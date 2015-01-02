package modele;

import java.io.Serializable;

public class Categorie implements Serializable {

	protected int id_categorie = -1;
	protected String label;
	protected String couleur;

	public Categorie() {

	}

	public Categorie(int id_categorie) {
		this.id_categorie = id_categorie;
	}

	public Categorie(String label, String couleur) {
		this.id_categorie = id_categorie;
		this.label = label;
		this.couleur = couleur;
	}

	public Categorie(int id_categorie, String label, String couleur) {
		this.id_categorie = id_categorie;
		this.label = label;
		this.couleur = couleur;
	}

	public int getId_categorie() {
		return id_categorie;
	}

	public String getLabel() {
		return label;
	}

	public String getCouleur() {
		return couleur;
	}

	public void setId_categorie(int id_categorie) {
		this.id_categorie = id_categorie;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}

	@Override
	public String toString() {
		return "Categorie{" + "id_categorie=" + id_categorie + ", label="
				+ label + ", couleur=" + couleur + '}';
	}
}
