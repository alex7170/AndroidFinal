package modele;

import java.io.Serializable;
import java.util.ArrayList;

class Carnet implements Serializable {

    protected int id_carnet;
    protected String titre;
    protected int id_user;
    protected ArrayList<NoteDB> listNote;

    public Carnet() {
        this.id_carnet = -1;
        this.titre = "";
        this.id_user = -1;
        this.listNote = new ArrayList<NoteDB>();
    }

    public Carnet(int id_carnet) {
        this.id_carnet = id_carnet;
        this.titre = "";
        this.id_user = -1;
        this.listNote = new ArrayList<NoteDB>();
    }

    public Carnet(String titre, int id_user) {
        this.id_carnet = -1;
        this.titre = titre;
        this.id_user = id_user;
        this.listNote = new ArrayList<NoteDB>();
    }

    public Carnet(String titre, int id_user, ArrayList<NoteDB> listNote) {
        this.id_carnet = -1;
        this.titre = titre;
        this.id_user = id_user;
        this.listNote = listNote;
    }

    public Carnet(int id_carnet, String titre, int id_user, ArrayList<NoteDB> listNote) {
        this.id_carnet = id_carnet;
        this.titre = titre;
        this.id_user = id_user;
        this.listNote = listNote;
    }

    public int getId_carnet() {
        return id_carnet;
    }

    public String getTitre() {
        return titre;
    }

    public int getId_user() {
        return id_user;
    }

    public ArrayList<NoteDB> getListNote() {
        return listNote;
    }

    public void setId_carnet(int id_carnet) {
        this.id_carnet = id_carnet;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setListNote(ArrayList<NoteDB> listNote) {
        this.listNote = listNote;
    }

    public void addNote(NoteDB obj) {
        listNote.add(obj);
    }

    @Override
    public String toString() {
        return "Carnet{" + "id_carnet=" + id_carnet + ", titre=" + titre + ", id_user=" + id_user + ", listNote=" + listNote + '}';
    }

}
