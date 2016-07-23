package es.victorpinal.adslwebapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.struts2.interceptor.ParameterAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class MainForm extends ActionSupport implements ParameterAware {

	private static final long serialVersionUID = 1L;
	private static final Logger _log = Logger.getLogger(MainForm.class.getName());

	/*********
	 * VARIABLES
	 */

	private Map<String, String[]> params;

	private Vector<Ip_Class> datosIp; // Listado con las ips de la BBDD
	private int idSelected; // id seleccionada en el form
	private String resumen; // resumen de los datos para la id seleccionada
	private Vector<String[]> datos; // datos para la id seleccionada

	/**************
	 * GETTERS/SETTERS
	 */

	public void setIdSelected(int id) {
		this.idSelected = id;
	}
	
	public int getIdSelected() {
	    return idSelected;
	}

	public Vector<Ip_Class> getDatosIp() {
		return datosIp;
	}

	public String getResumen() {
		return resumen;
	}

	public Vector<String[]> getDatos() {
		return datos;
	}
	
	@Override
	public void setParameters(Map<String, String[]> arg0) {
		params = arg0;		
	}

	/************
	 * BODY
	 */

	@Override
	public String execute() throws Exception {

		_log.entering(this.getClass().getName(), "execute");

		datosIp = new Vector<>();
		try (ResultSet res = mySQL.get().getConnection().createStatement().executeQuery("SELECT * FROM ip")) {
			while (res.next()) {
				datosIp.addElement(new Ip_Class(res.getInt("id"), res.getString("ip"), res.getString("name")));
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "Error leyendo ips", e);
		}

		if (idSelected == 0) { //la primera vez buscamos los datos locales
		    getLocalData();
		}
		
		cargaResumen();
		cargaDatos();

		_log.exiting(this.getClass().getName(), "execute");

		return SUCCESS;
	}
	
	private void getLocalData() throws UnknownHostException {
	    
	 // buscamos la Ip_Class externa del equipo
        String ip = "127.0.0.1";
        String hostname = InetAddress.getLocalHost().getHostName();
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine(); // you get the IP as a String
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        for (Ip_Class e : datosIp) {
            if (e.equals(new Ip_Class(0, ip, hostname)))
                idSelected = e.getId();
        }
	    
	}

	public String cargaDatos() {

		//String[] id = params.get("id");
		datos = new Vector<String[]>();
		// añadimos las cabeceras
		datos.add(new String[] { "Hora", "Down", "Up", "Att.Down", "Att.Up" });

		try (PreparedStatement stmt = mySQL.get().getConnection().prepareStatement(
				"SELECT time,download,upload,attdownrate,attuprate FROM datos WHERE ip_id=? ORDER BY time DESC")) {

			stmt.setInt(1, idSelected);
			ResultSet res = stmt.executeQuery();
			
			while (res.next()) {
				String[] datos_temp = new String[5];
				datos_temp[0] = new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("time"));
				datos_temp[1] = res.getString("download");
				datos_temp[2] = res.getString("upload");
				datos_temp[3] = res.getString("attdownrate");
				datos_temp[4] = res.getString("attuprate");
				datos.add(datos_temp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
		return SUCCESS;

	}

	public String cargaResumen() {

		try (PreparedStatement stmt = mySQL.get().getConnection()
				.prepareStatement("SELECT * FROM resumen WHERE ip_id=?")) {

			stmt.setInt(1, idSelected);
			ResultSet res = stmt.executeQuery();

			StringBuilder sb = new StringBuilder();
			Formatter formatter = new Formatter(sb, Locale.getDefault());

			while (res.next()) {

				formatter.format("%6s registros %n", res.getInt("NumRecords"));
				formatter.format("%6s días desde %s %n", res.getInt("NumDays"),
						new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("Min_Date")));
				formatter.format("%nUltimo %s %n",
						new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("Max_Date")));

				formatter.format("%nSNR   DOWNLOAD          UPLOAD %n");
				formatter.format(" MAX  %8s(%3s) %10s(%3s) %n", res.getInt("Max_DOWN_SNR"),
						res.getInt("LAST_DOWN_SNR") - res.getInt("Max_DOWN_SNR"), res.getInt("Max_UP_SNR"),
						res.getInt("LAST_UP_SNR") - res.getInt("Max_UP_SNR"));
				formatter.format(" MIN  %8s(%3s) %10s(%3s) %n", res.getInt("Min_DOWN_SNR"),
						res.getInt("LAST_DOWN_SNR") - res.getInt("Min_DOWN_SNR"), res.getInt("Min_UP_SNR"),
						res.getInt("LAST_UP_SNR") - res.getInt("Min_UP_SNR"));
				formatter.format(" AVG  %8s(%3s) %10s(%3s) %n", res.getInt("Avg_DOWN_SNR"),
						res.getInt("LAST_DOWN_SNR") - res.getInt("Avg_DOWN_SNR"), res.getInt("Avg_UP_SNR"),
						res.getInt("LAST_UP_SNR") - res.getInt("Avg_UP_SNR"));
				formatter.format(" LAST %8s %15s %n", res.getInt("LAST_DOWN_SNR"), res.getInt("LAST_UP_SNR"));

				formatter.format("%nATT   DOWNLOAD          UPLOAD %n");
				formatter.format(" MAX  %8s(%5s) %8s(%5s) %n", res.getInt("Max_DOWN"),
						res.getInt("LAST_DOWN") - res.getInt("Max_DOWN"), res.getInt("Max_UP"),
						res.getInt("LAST_UP") - res.getInt("Max_UP"));
				formatter.format(" MIN  %8s(%5s) %8s(%5s) %n", res.getInt("Min_DOWN"),
						res.getInt("LAST_DOWN") - res.getInt("Min_DOWN"), res.getInt("Min_UP"),
						res.getInt("LAST_UP") - res.getInt("Min_UP"));
				formatter.format(" AVG  %8s(%5s) %8s(%5s) %n", res.getInt("Avg_DOWN"),
						res.getInt("LAST_DOWN") - res.getInt("Avg_DOWN"), res.getInt("Avg_UP"),
						res.getInt("LAST_UP") - res.getInt("Avg_UP"));
				formatter.format(" LAST %8s %15s %n", res.getInt("LAST_DOWN"), res.getInt("LAST_UP"));

				formatter.format("%nPWR   DOWNLOAD          UPLOAD %n");
				formatter.format(" MAX  %8s %15s %n", res.getInt("Max_DOWN_Power"), res.getInt("Max_UP_Power"));
				formatter.format(" MIN  %8s %15s %n", res.getInt("Min_DOWN_Power"), res.getInt("Min_UP_Power"));
			}
			formatter.close();

			resumen = sb.toString();

			return SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
			return ERROR;
		}

	}

}
