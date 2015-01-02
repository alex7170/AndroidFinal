package modele;
import java.io.Serializable;
import java.sql.Date;

public class Note implements Serializable {

    protected int id_note;
    protected String titre;
    protected String contenu;
    protected Date date_note;
    protected int id_carnet;
    protected CategorieDB categorie;

    public Note() {
    }

    public Note(int id_note) {
        this.id_note = id_note;
    }

    public Note(String titre, String contenu, Date date_note, int id_carnet, CategorieDB categorie) {
        this.titre = titre;
        this.contenu = contenu;
        this.date_note = date_note;
        this.id_carnet = id_carnet;
        this.categorie = categorie;
    }

    public Note(int id_note, String titre, String contenu, Date date_note, int id_carnet, CategorieDB categorie) {
        this.id_note = id_note;
        this.titre = titre;
        this.contenu = contenu;
        this.date_note = date_note;
        this.id_carnet = id_carnet;
        this.categorie = categorie;
    }

    public int getId_note() {
        return id_note;
    }

    public String getTitre() {
        return titre;
    }

    public String getContenu() {
        return contenu;
    }

    public Date getDate_note() {
        return date_note;
    }

    public int getId_carnet() {
        return id_carnet;
    }


    public void setId_note(int id_note) {
        this.id_note = id_note;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setDate_note(Date date_note) {
        this.date_note = date_note;
    }

    public void setId_carnet(int id_carnet) {
        this.id_carnet = id_carnet;
    }

    public CategorieDB getCategorie() {
		return categorie;
	}

	public void setCategorie(CategorieDB categorie) {
		this.categorie = categorie;
	}

	@Override
    public String toString() {
        return "Note{" + "id_note=" + id_note + ", titre=" + titre + ", contenu=" + contenu + ", date_note=" + date_note + ", id_carnet=" + id_carnet + ", categorie=" + categorie + '}';
    }

}
