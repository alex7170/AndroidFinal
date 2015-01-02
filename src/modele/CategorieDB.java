package modele;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CategorieDB extends Categorie implements CRUD, Serializable {

	protected static Connection dbConnect = null;

	public static void setConnection(Connection nouvdbConnect) {
		dbConnect = nouvdbConnect;
	}

	public CategorieDB() {

	}

	public CategorieDB(int id_categorie) {
		super(id_categorie);
	}

	public CategorieDB(String label, String couleur) {
		super(label, couleur);
	}

	public CategorieDB(int id_categorie, String label, String couleur) {
		super(id_categorie, label, couleur);
	}

	@Override
	public void create() throws Exception {
		CallableStatement cstmt = null;
		try {
			String query1 = "call createcategorie(?,?)";
			String query2 = "select cat_seq.currval from dual";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			PreparedStatement pstm2 = dbConnect.prepareStatement(query2);
			pstm1.setString(1, label);
			pstm1.setString(2, couleur);
			int nl = pstm1.executeUpdate();
			ResultSet rs = pstm2.executeQuery();
			if (rs.next()) {
				int nc = rs.getInt(1);
				id_categorie = nc;
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
			String query1 = "SELECT * FROM categorie WHERE id_categorie = ?";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_categorie);
			ResultSet rs = pstm1.executeQuery();
			if (rs.next()) {
				trouve = true;
				id_categorie = rs.getInt("ID_CATEGORIE");
				label = rs.getString("LABEL");
				couleur = rs.getString("COULEUR");
			}
			if (!trouve) {
				id_categorie = -1;
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

	@Override
	public void update() throws Exception {
		CallableStatement cstmt = null;

		try {
			String query1 = "call UpdateCategorie(?,?,?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_categorie);
			pstm1.setString(2, label);
			pstm1.setString(3, couleur);
			int nl = pstm1.executeUpdate();

		} catch (Exception e) {

			throw new Exception("Erreur de mise à jour : " + e.getMessage());
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
			String query1 = "call DeleteCategorie(?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_categorie);
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

	public static ArrayList<CategorieDB> getFull() throws Exception {
		ArrayList<CategorieDB> list = new ArrayList();
		CallableStatement cstmt = null;
		try {
			boolean trouve = false;
			String query1 = "select * from categorie order by id_categorie asc";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			ResultSet rs = pstm1.executeQuery();
			while (rs.next()) {
				trouve = true;
				list.add(new CategorieDB(rs.getInt(1), rs.getString(2), rs
						.getString(3)));
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
