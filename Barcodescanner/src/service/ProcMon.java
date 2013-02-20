package service;

public class ProcMon implements Runnable {
	private final Process _proc;
	private volatile boolean _complete;
	
	public ProcMon(Process p) {
		_proc = p;
		_complete = false;
	}
	
	public boolean isComplete() { return _complete; }
	
	public void run() {
		try {
			_proc.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		_complete = true;
	}
	
	public static ProcMon create(Process proc) {
		ProcMon procMon = new ProcMon(proc);
		Thread t = new Thread (procMon);
		t.start();
		return procMon;
	}
}
