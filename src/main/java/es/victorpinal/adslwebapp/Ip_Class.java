package es.victorpinal.adslwebapp;

/**
 * Created by victo on 04/12/2015.
 */
public class Ip_Class {

	private int id;
	private String ip;
	private String name;

	public Ip_Class(int id, String ip, String name) {
		this.id = id;
		this.ip = ip;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getIp() {
		return ip;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + " - " + ip;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Ip_Class ip_class = (Ip_Class) o;

		return (ip != null ? ip.equals(ip_class.ip) : ip_class.ip == null)
				&& (name != null ? name.equals(ip_class.name) : ip_class.name == null);

	}

	@Override
	public int hashCode() {
		return (ip != null ? ip.hashCode() : 0) + (name != null ? name.hashCode() : 0);
	}
}
