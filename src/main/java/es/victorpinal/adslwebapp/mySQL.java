package es.victorpinal.adslwebapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class mySQL {

    private static final mySQL instance = new mySQL();
    private static final Logger _log = Logger.getLogger(mySQL.class.getName());
    private String connectionString;
    

    private mySQL() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            _log.log(Level.SEVERE, "Driver no encontrado", e);
        }

    }
    
    private void loadConnectionString() throws PreferenceException {
        
        // Leemos los datos de la conexión de las preferencias de usuario
        Preferences pref = Preferences.userNodeForPackage(mySQL.class);
        if (pref.get("servidor", null) == null || pref.get("password", null) == null) {
            _log.log(Level.SEVERE, "readPreferences -> preferencias no encontradas");
            throw new PreferenceException();
        } else {
            this.connectionString = String.format("jdbc:mysql://%1$s:%2$s/adsl?user=%3$s&password=%4$s", pref.get("servidor", "localhost"),
                    pref.get("puerto", "3306"), pref.get("usuario", null), pref.get("password", null)).toString();
        }

    }

    public void writePreferences(Preferences arg) {

        Preferences pref = Preferences.userNodeForPackage(mySQL.class);
        pref.put("servidor", arg.get("servidor", null));
        pref.put("puerto", arg.get("puerto", null));
        pref.put("usuario", arg.get("usuario", null));
        pref.put("password", arg.get("password", null));

    }
    
    public static mySQL getMySQL() {
        return instance;
    }

    public Connection getConnection() throws SQLException,PreferenceException {

        if (connectionString != "") {
            return DriverManager.getConnection(connectionString);
        } else {            
            loadConnectionString();
            _log.log(Level.SEVERE, "getConnection -> connection string vacía");
            return null;
        }

    }

}
