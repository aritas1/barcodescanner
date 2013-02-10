
// This little utility opens /lib/keymap -i barcodescanner
// and reads the output it produces and parses it for keys.
// The results are spilled out to stdout and to /tmp/barcodescanner.txt
// Hacked by Jonas :-)

#include <stdlib.h>
#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <string.h>
#include <termios.h>
#include <pty.h>

FILE* r = NULL;
pid_t childprocess = 0;
void quithandler(int sig) {
    fprintf(stderr, "Exiting...\n");
    fclose(r);
    exit(0);
}

void outputkey(char c) {
    // write key to file:
    fwrite(&c, 1, 1, r);
    if (c == '\n') {
        fflush(r);
    }

    // write key to stdout:
    fprintf(stdout, "%c", c);

    // ... do something else here at some point?
    if (c == '\n') {
        fflush(stdout);
        // new entry added, run script:
        //system("python /home/daniel/blub.py");
    }
}

int main(int argc, char** argv) {
    if ((getuid ()) != 0) {
        fprintf(stderr, "WARNING: Not running as root (device access might fail)\n");
    }

    // set signal handlers:
    signal(SIGINT, quithandler);
    signal(SIGTERM, quithandler);

    // we want to run "/lib/udev/keymap -i barcodescanner" and
    // parse the output for the key presses.

    // fork and start process in forked child:
    // (we use forkpty so our child process's stdout won't be block buffered)
    int fd;
    pid_t pid = forkpty(&fd, NULL, NULL, NULL);
    if (pid < 0) {
        fprintf(stderr, "fork() returned an error\n");
        return 1;
    } else if (pid == 0) {
        // child process, run program here after preparing fake terminal:
        // (which we need because otherwise we get block buffering)
        // disable the terminal's echo functionality:
        struct termios termiosattr;
        if (tcgetattr(STDIN_FILENO, &termiosattr) < 0) {
            fprintf(stderr, "tcgetattr() failed");
            return 1;
        }
        termiosattr.c_lflag &= ~(ECHO | ECHOE | ECHOK | ECHONL);
        if (tcsetattr(STDIN_FILENO, TCSANOW, &termiosattr) < 0) {
            fprintf(stderr, "tcsetattr() failed");
            return 1;
        }

        // run program:
        const char* argv[] = { "/lib/udev/keymap", "-i", "barcodescanner", NULL };
        if (execv("/lib/udev/keymap", (char* const*) argv) < 0) {
            exit(1);
        }
        exit(0);
    } else {
        // parent process
        childprocess = pid;

        // open output file:
        r = fopen("/tmp/barcodescanner.txt", "wb");
        if (!r) {
            fprintf(stderr, "Failed to open output file\n");
            exit(1);
        }

        // prepare read buffer:
        char linebuf[512] = "";
        int oldlen = 0;
        int nextkeycapitalised = 0;
        fprintf(stderr, "Debug: now reading data...\n");

        // read /lib/udev/keymap output byte per byte:
        int rv;
        while (strlen(linebuf) < sizeof(linebuf)-2 &&
        (rv = read(fd, linebuf+oldlen, 1)) > 0) {
           // we received data. null terminate:
           //fprintf(stderr, "Debug: read result: %d\n", rv);
           linebuf[oldlen+rv] = 0;
           oldlen = strlen(linebuf);
           //fprintf(stderr, "Debug: received data. current buffer contents: %s\n", linebuf);

           // check for completed line:
           if (strlen(linebuf) > 0 && linebuf[strlen(linebuf)-1] == '\n') {
               // remove line break:
               linebuf[strlen(linebuf)-1] = 0;
               if (strlen(linebuf) > 0 && linebuf[strlen(linebuf)-1] == '\r') {
                   linebuf[strlen(linebuf)-1] = 0;
               }

               // check for magic "key code: " phrase:
               char* p = strstr(linebuf, "key code: ");
               if (p) {
                   // present. check for key:
                   p += strlen("key code: ");

                   // check if it's a multi-byte special key:
                   if (strlen(p) > 1) {
                       // it is. we cover a few variants here:
                       // (which we need to turn back into usable chars)
                       if (strcmp(p, "enter") == 0) {
                           outputkey('\n');
                       } else if (strcmp(p, "leftshift") == 0) {
                           nextkeycapitalised = 1;
                       } else {
                           // no idea what this means:
                           fprintf(stderr, "Unknown multi-byte key: \"%s\"\n", p);
                       }
                   } else {
                       // output the single byte key,
                       // and capitalise it if shift was pressed before
                       if (nextkeycapitalised) {
                           outputkey(toupper(p[0]));
                       } else {
                           outputkey(p[0]);
                       }
                       nextkeycapitalised = 0;
                   }
               }
               // we are done with the line in linebuf.
               // forget about it and continue checking linebuf for more lines
               linebuf[0] = 0;
               oldlen = 0;
           }
           // -> read more input into linebuf.
        }
        fprintf(stderr, "Read error (e.g. EOF)\n");
        return 1;
    }
    
    return 0;
}
