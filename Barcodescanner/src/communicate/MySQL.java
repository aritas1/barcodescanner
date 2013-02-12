package communicate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

   private String Username = "barcodescanner";
   private String Password = "8jv7aw7Qe4Mh4Yha";
   private String Driver = "com.mysql.jdbc.Driver";
   private String Host = "192.168.1.111";
   private String DBName = "barcodescanner";
   private String URL = "jdbc:mysql://"+Host+"/"+DBName;
   private Connection connection;

   public MySQL() {
       this.Connect();
   }
  /* Sollte der Konstruktur ohne argumente aufgerufen werden, werden die in der klasse genutzten Werte genommen. */


   public MySQL(String user, String pass) {
       this.Username = user;
       this.Password = pass;
       this.Connect();
   }

/* Sollte der Konstruktur mit den Argumenten user und pass aufgerufen werden, werden diese definiert und dann Verbunden. */

   public void Close() {
       if (this.connection != null) {
           try {
               this.connection.close();
           } catch (Exception e) {
           }
       }
   }

/* Die Funktion Close() schließt das Query um den Speicher wieder frei zu geben */

   public void Connect() {
       try {
           Class.forName(this.Driver);
           this.connection = DriverManager.getConnection(this.URL,
                   this.Username, this.Password);
       } catch (Exception e) {
           e.printStackTrace();
           System.out.println(e.toString());
           System.out.println("Error Connecting with User:" + Username + " and Password:" + Password);
       }
   }

/* Connect registriert den JDBC Treiber und versucht eine Verbindung herzustellen. Sollte dies nicht möglich sein, wird eine Exception ausgelöst */

   public boolean isConnected() {
       try {
           ResultSet rs = this.ReturnQuery("SELECT 1;");
           if (rs == null) {
               return false;
           }
           if (rs.next()) {
               return true;
           }
           return false;
       } catch (Exception e) {
           return false;
       }
   }

/* frägt ein einfaches Query ab, welches "1" zurück liefert, falls man verbunden ist */

   public ResultSet ReturnQuery(String query) {
       try {
           Statement stmt = this.connection.createStatement();
           ResultSet rs = stmt.executeQuery(query);
           return rs;
       } catch (SQLException e) {
           System.err.println(e.toString());
           return null;
       }
   }

/* Sendet ein Query und erwartet eine Rückgabe in Form eines ResultSet */

   public boolean RunQuery(String query) {
       try {
           Statement stmt = this.connection.createStatement();
           return stmt.execute(query);
       } catch (Exception e) {
           //  e.printStackTrace();
           return false;
       }
   }
/* Führt das query aus, erwartet aber keine Rückantwort des Servers. */

}
