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
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Created by victormanuel on 03/12/2015.
 */
public class MainForm extends ActionSupport implements ParameterAware, ServletRequestAware {

    private static final long serialVersionUID = 1L;
    private static final Logger _log = Logger.getLogger(MainForm.class.getName());
    private static final String ERROR_CONEXION = "error_conexion";

    /*********
     * VARIABLES
     */

    private static final mySQL my_SQL = mySQL.getInstance();
    private Map<String, String[]> params;
    private DatosConexion datosConexion;

    private Vector<Ip_Class> datosIp;           // Listado con las ips de la BBDD
    private int idSelected;                     // id seleccionada en el form
    private Date fechaIni = Calendar.getInstance().getTime();
    private Date fechaFin = Calendar.getInstance().getTime();   // Fechas de inicio y fin de la muestra
    private String resumen;                     // resumen de los datos para la id seleccionada
    private Vector<String[]> datos;             // datos para la id seleccionada
    private HttpServletRequest request;

    /**************
     * GETTERS/SETTERS
     */

    public void setDatosConexion(DatosConexion datos) {
        datosConexion = datos;
    }

    public DatosConexion getDatosConexion() {
        return datosConexion;
    }

    public void setIdSelected(int id) {
        this.idSelected = id;
    }

    public int getIdSelected() {
        return idSelected;
    }

    public void setFechaIni(Date value) {
        fechaIni = value;
    }

    public String getFechaIni() {
        return new SimpleDateFormat("yyyy-MM-dd").format(fechaIni);
    }

    public void setFechaFin(Date value) {
        fechaFin = value;
    }

    public String getFechaFin() {
        return new SimpleDateFormat("yyyy-MM-dd").format(fechaFin);
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
    public String execute() {

        _log.entering(this.getClass().getName(), "execute");

        if (datosConexion != null) { // Si nos pasan datos nuevos de conexiÃƒÆ’Ã‚Â³n los guardamos
            my_SQL.writePreferences(datosConexion);
        }

        try { // Cargamos los datos de las IPs
            getListaIPs();
        } catch (Exception e) {
            return ERROR_CONEXION;
        }

        if (idSelected == 0) { // la primera vez buscamos los datos locales

            getLocalData();

        } else {

            if (fechaIni == null || fechaFin == null) { // Buscamos las fechas de inicio y de fin
                getFechas();
            }

            try { // Cargamos los datos
                cargaResumen();
                cargaDatos();
            } catch (Exception e) {
                return ERROR;
            }

        }

        _log.exiting(this.getClass().getName(), "execute");

        return SUCCESS;
    }

    private void getListaIPs() throws Exception {
        
        datosIp = new Vector<>();

        try (ResultSet res = my_SQL.getConnection().createStatement().executeQuery("SELECT * FROM ip")) {
            while (res.next()) {
                datosIp.addElement(new Ip_Class(res.getInt("id"), res.getString("ip"), res.getString("name")));
            }
        } catch (Exception e) {
            if (e instanceof PreferenceException) {
                _log.severe("Error creando conexiÃƒÆ’Ã‚Â³n, no existen los parametros de conexiÃƒÆ’Ã‚Â³n mySQL");
            } else {
                _log.severe("Error leyendo ips");
            }
            throw e;
        }

    }

    private void getFechas() {

        try {
            PreparedStatement stmt = my_SQL.getConnection().prepareStatement("SELECT MAX(time) fin, MIN(time) inicio FROM datos WHERE ip_id=?");
            stmt.setInt(1, idSelected);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                if (fechaIni == null)
                    fechaIni = res.getDate("inicio");
                if (fechaFin == null)
                    fechaFin = res.getDate("fin");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getLocalData() {

        // buscamos la Ip_Class externa del equipo
        String ip = "127.0.0.1";
        String hostname = "localhost";

        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            _log.severe("getLocalData -> No ha sido posible obtener el nombre de host");
        }

        /*try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            ip = in.readLine(); // you get the IP as a String
        } catch (IOException e) {
            _log.severe("getLocalData -> No ha sido posible obtener la ip del host");
        }*/
        
        ip = request.getRemoteAddr();
        hostname = request.getRemoteHost();
        _log.severe("getLocalData -> " + request.getRemoteAddr() + " - " + request.getRemoteHost());

        for (Ip_Class e : datosIp) {
            if (e.equals(new Ip_Class(0, ip, hostname)))
                idSelected = e.getId();
        }

    }

    private void cargaDatos() throws Exception {

        // String[] id = params.get("id");
        datos = new Vector<String[]>();
        // aÃƒÆ’Ã‚Â±adimos las cabeceras
        // datos.add(new String[] { "Hora", "Down", "Up", "Att.Down", "Att.Up"
        // });

        PreparedStatement stmt = my_SQL.getConnection()
                .prepareStatement("SELECT time,download,upload,attdownrate,attuprate FROM datos WHERE ip_id=? AND time BETWEEN ? AND ? ORDER BY time DESC");

        stmt.setInt(1, idSelected);
        stmt.setDate(2, new java.sql.Date(fechaIni.getTime()));
        stmt.setDate(3, new java.sql.Date(fechaFin.getTime()));
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

    }

    private void cargaResumen() throws Exception {

        PreparedStatement stmt = my_SQL.getConnection().prepareStatement("SELECT * FROM resumen WHERE ip_id=?");

        stmt.setInt(1, idSelected);
        ResultSet res = stmt.executeQuery();

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.getDefault());

        while (res.next()) {

            formatter.format("%6s registros %n", res.getInt("NumRecords"));
            formatter.format("%6s dÃ­as desde %s %n", res.getInt("NumDays"), new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("Min_Date")));
            formatter.format("%nUltimo %s %n", new SimpleDateFormat("dd/MM/yy HH:mm").format(res.getTimestamp("Max_Date")));

            formatter.format("%nSNR   DOWNLOAD          UPLOAD %n");
            formatter.format(" MAX  %8s(%3s) %10s(%3s) %n", res.getInt("Max_DOWN_SNR"), res.getInt("LAST_DOWN_SNR") - res.getInt("Max_DOWN_SNR"),
                    res.getInt("Max_UP_SNR"), res.getInt("LAST_UP_SNR") - res.getInt("Max_UP_SNR"));
            formatter.format(" MIN  %8s(%3s) %10s(%3s) %n", res.getInt("Min_DOWN_SNR"), res.getInt("LAST_DOWN_SNR") - res.getInt("Min_DOWN_SNR"),
                    res.getInt("Min_UP_SNR"), res.getInt("LAST_UP_SNR") - res.getInt("Min_UP_SNR"));
            formatter.format(" AVG  %8s(%3s) %10s(%3s) %n", res.getInt("Avg_DOWN_SNR"), res.getInt("LAST_DOWN_SNR") - res.getInt("Avg_DOWN_SNR"),
                    res.getInt("Avg_UP_SNR"), res.getInt("LAST_UP_SNR") - res.getInt("Avg_UP_SNR"));
            formatter.format(" LAST %8s %15s %n", res.getInt("LAST_DOWN_SNR"), res.getInt("LAST_UP_SNR"));

            formatter.format("%nATT   DOWNLOAD          UPLOAD %n");
            formatter.format(" MAX  %8s(%5s) %8s(%5s) %n", res.getInt("Max_DOWN"), res.getInt("LAST_DOWN") - res.getInt("Max_DOWN"), res.getInt("Max_UP"),
                    res.getInt("LAST_UP") - res.getInt("Max_UP"));
            formatter.format(" MIN  %8s(%5s) %8s(%5s) %n", res.getInt("Min_DOWN"), res.getInt("LAST_DOWN") - res.getInt("Min_DOWN"), res.getInt("Min_UP"),
                    res.getInt("LAST_UP") - res.getInt("Min_UP"));
            formatter.format(" AVG  %8s(%5s) %8s(%5s) %n", res.getInt("Avg_DOWN"), res.getInt("LAST_DOWN") - res.getInt("Avg_DOWN"), res.getInt("Avg_UP"),
                    res.getInt("LAST_UP") - res.getInt("Avg_UP"));
            formatter.format(" LAST %8s %15s %n", res.getInt("LAST_DOWN"), res.getInt("LAST_UP"));

            formatter.format("%nPWR   DOWNLOAD          UPLOAD %n");
            formatter.format(" MAX  %8s %15s %n", res.getInt("Max_DOWN_Power"), res.getInt("Max_UP_Power"));
            formatter.format(" MIN  %8s %15s %n", res.getInt("Min_DOWN_Power"), res.getInt("Min_UP_Power"));
        }
        formatter.close();

        resumen = sb.toString();

    }

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

}
