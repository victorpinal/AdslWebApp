package es.victorpinal.adslwebapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class mySQL {

    private static final mySQL instance = new mySQL();
    private static final Logger _log = Logger.getLogger(mySQL.class.getName());
	private final String connectionString;

    private mySQL() {

    	/*
    	// Read connection data from preferences
		Preferences preferences = Preferences.userNodeForPackage(mySQL.class);
        if (preferences.get("servidor", null) == null || preferences.get("password", null) == null) {
            preferences.put("servidor", JOptionPane.showInputDialog(null, "Servidor MySQL", "Configuracion", JOptionPane.QUESTION_MESSAGE));
            preferences.put("puerto", JOptionPane.showInputDialog(null, "Puerto", "Configuracion", JOptionPane.QUESTION_MESSAGE));
            preferences.put("usuario", JOptionPane.showInputDialog(null, "Usuario", "Configuracion", JOptionPane.QUESTION_MESSAGE));
            preferences.put("password", JOptionPane.showInputDialog(null, "Password", "Configuracion", JOptionPane.QUESTION_MESSAGE));
        }

        connectionString = String.format("jdbc:mysql://%1$s:%2$s/adsl?user=%3$s&password=%4$s",
                preferences.get("servidor", "localhost"),
                preferences.get("puerto", "3306"),
                preferences.get("usuario", null),
                preferences.get("password", null)).toString();
        */

        connectionString = String.format("jdbc:mysql://localhost:3306/adsl?user=victor&password=vistorr");
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            _log.log(Level.SEVERE, "Driver no encontrado", e);
        }

    }

    public static mySQL get() {
        return instance;
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(connectionString);

    }

}
