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

    private static mySQL instance;
    private static final Logger _log = Logger.getLogger(mySQL.class.getName());
    private String connectionString;
    

    private mySQL() {
    	
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            _log.severe("mySQL -> Driver no encontrado");
        }

    }
    
    private void loadConnectionString() throws PreferenceException {
        
        // Leemos los datos de la conexión de las preferencias de usuario
        Preferences pref = Preferences.userNodeForPackage(mySQL.class);
        if (pref.get("servidor", null) == null || pref.get("usuario", null) == null || pref.get("password", null) == null) {
            _log.info("mySQL -> Preferencias no encontradas");
            throw new PreferenceException();
        } else {
            this.connectionString = String.format("jdbc:mysql://%1$s:%2$s/adsl?user=%3$s&password=%4$s", pref.get("servidor", "localhost"),
                    pref.get("puerto", "3306"), pref.get("usuario", null), pref.get("password", null)).toString();
        }

    }

    public void writePreferences(DatosConexion arg) {

        _log.info("mySQL -> Guardando las preferencias de conexión.");
        Preferences pref = Preferences.userNodeForPackage(mySQL.class);
        pref.put("servidor", arg!=null?arg.getMysqlserver():"");
        pref.put("puerto", arg!=null?arg.getMysqlport():"3306");
        pref.put("usuario", arg!=null?arg.getMysqluser():"");
        pref.put("password", arg!=null?arg.getMysqlpwd():"");

    }
    
    public static mySQL getInstance() {      
        _log.info("mySQL -> getInstance()");
        return instance==null?new mySQL():instance;
    }

    public Connection getConnection() throws SQLException,PreferenceException {

        if (connectionString!=null && connectionString!= "") {
            return DriverManager.getConnection(connectionString);
        } else {                        
            _log.info("mySQL -> connectionstring vacía ...");
            loadConnectionString();            
            return getConnection();
        }

    }

}
