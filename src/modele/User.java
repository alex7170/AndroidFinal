package modele;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

	protected int id_user;
	protected String pseudo;
	protected String mail;
	protected String password;
	protected ArrayList<CarnetDB> listCarnet;

	public User(int id_user) {
		this.id_user = id_user;
		this.pseudo = "";
		this.mail = "";
		this.password = "";
		this.listCarnet = new ArrayList();
	}

	public User(String mail, String password) {
		this.id_user = -1;
		this.pseudo = "";
		this.mail = mail;
		this.password = password;
		this.listCarnet = new ArrayList();
	}

	public User(String pseudo, String mail, String password) {
		this.id_user = -1;
		this.pseudo = pseudo;
		this.mail = mail;
		this.password = password;
		this.listCarnet = new ArrayList();
	}

	public User(int id_user, String pseudo, String mail, String password) {
		this.id_user = id_user;
		this.pseudo = pseudo;
		this.mail = mail;
		this.password = password;
		this.listCarnet = new ArrayList();
	}

	public int getId_user() {
		return id_user;
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getMail() {
		return mail;
	}

	public String getPassword() {
		return password;
	}

	public ArrayList<CarnetDB> getListCarnet() {
		return listCarnet;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setListCarnet(ArrayList<CarnetDB> listCarnet) {
		this.listCarnet = listCarnet;
	}

	public void addCarnet(CarnetDB obj) {
		listCarnet.add(obj);
	}

	@Override
	public String toString() {
		return "User{" + "id_user=" + id_user + ", pseudo=" + pseudo
				+ ", mail=" + mail + ", password=" + password + ", listCarnet="
				+ listCarnet + '}';
	}

}
