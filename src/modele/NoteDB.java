package modele;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NoteDB extends Note implements CRUD, Serializable {

	protected static Connection dbConnect = null;

	public NoteDB() {
		super();
	}

	public NoteDB(int id_note) {
		super(id_note);
	}

	public NoteDB(String titre, String contenu, Date date_note, int id_carnet,
			CategorieDB categorie) {
		super(titre, contenu, date_note, id_carnet, categorie);
	}

	public NoteDB(int id_note, String titre, String contenu, Date date_note,
			int id_carnet, CategorieDB categorie) {
		super(id_note, titre, contenu, date_note, id_carnet, categorie);
	}

	public static void setConnection(Connection nouvdbConnect) {
		dbConnect = nouvdbConnect;
	}

	@Override
	public void create() throws Exception {
		CallableStatement cstmt = null;
		try {
			String query1 = "call createNote(?,?,?,?,?)";
			String query2 = "select note_seq.currval from dual";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			PreparedStatement pstm2 = dbConnect.prepareStatement(query2);
			pstm1.setString(1, titre);
			pstm1.setString(2, contenu);
			pstm1.setDate(3, date_note);
			pstm1.setInt(4, id_carnet);
			pstm1.setInt(5, categorie.getId_categorie());
			int nl = pstm1.executeUpdate();
			ResultSet rs = pstm2.executeQuery();
			if (rs.next()) {
				int nc = rs.getInt(1);
				id_note = nc;
			} else {
				System.out.println("Erreur de l'ajout");
			}

		} catch (Exception e) {
			throw new Exception("Erreur de cr�ation " + e.getMessage());
		} finally {
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void read() throws Exception {

		CallableStatement cstmt = null;
		try {
			boolean trouve = false;
			String query1 = "SELECT note.*, categorie. FROM note, categorie WHERE note.id_note = ? and note.id_categorie = categorie.id_categorie";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_note);
			ResultSet rs = pstm1.executeQuery();
			if (rs.next()) {
				trouve = true;
				id_note = rs.getInt("ID_NOTE");
				titre = rs.getString("TITRE");
				contenu = rs.getString("CONTENU");
				date_note = rs.getDate("DATE_NOTE");
				id_carnet = rs.getInt("ID_CARNET");
				categorie = new CategorieDB(rs.getInt("ID_CARNET"),
						rs.getString("LABEL"), rs.getString("COULEUR"));
			}
			if (!trouve) {
				id_note = -1;
				throw new Exception("numero inconnu dans la table !");
			}
		} catch (Exception e) {

			throw new Exception("Erreur de lecture " + e.getMessage());
		} finally {
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 *
	 * @throws Exception
	 *             erreur de mise � jour
	 */
	@Override
	public void update() throws Exception {
		CallableStatement cstmt = null;

		try {
			String query1 = "call UpdateNote(?,?,?,?,?,?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_note);
			pstm1.setString(2, titre);
			pstm1.setString(3, contenu);
			pstm1.setDate(4, date_note);
			pstm1.setInt(5, id_carnet);
			pstm1.setInt(6, categorie.getId_categorie());
			int nl = pstm1.executeUpdate();

		} catch (Exception e) {

			throw new Exception("Erreur de mise � jour : " + e.getMessage());
		} finally {// effectu� dans tous les cas
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void delete() throws Exception {

		CallableStatement cstmt = null;
		try {
			String query1 = "call DeleteNote(?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_note);
			int nl = pstm1.executeUpdate();

		} catch (Exception e) {

			throw new Exception("Erreur d'effacement : " + e.getMessage());
		} finally {
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static ArrayList<NoteDB> getCarnet(int var) throws Exception {
		ArrayList<NoteDB> list = new ArrayList();
		CallableStatement cstmt = null;
		try {
			boolean trouve = false;
			String query1 = "select note.*, categorie.label, categorie.couleur from note, categorie where id_carnet = ? and note.id_categorie = categorie.id_categorie";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, var);
			ResultSet rs = pstm1.executeQuery();
			while (rs.next()) {
				trouve = true;
				list.add(new NoteDB(rs.getInt(1), rs.getString(2), rs
						.getString(3), rs.getDate(4), rs.getInt(5),
						new CategorieDB(rs.getInt(6), rs.getString(7), rs
								.getString(8))));
			}

			return list;
		} catch (Exception e) {
			throw new Exception("Erreur: " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}

	}
}
