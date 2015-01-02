package modele;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CarnetDB extends Carnet implements CRUD, Serializable {

	protected static Connection dbConnect = null;

	public CarnetDB() {
	}

	public CarnetDB(int id_carnet) {
		super(id_carnet);
	}

	public CarnetDB(String titre, int id_user) {
		super(titre, id_user);
	}

	public CarnetDB(String titre, int id_user, ArrayList<NoteDB> listNote) {
		super(titre, id_user, listNote);
	}

	public CarnetDB(int id_carnet, String titre, int id_user,
			ArrayList<NoteDB> listNote) {
		super(id_carnet, titre, id_user, listNote);
	}

	public static void setConnection(Connection nouvdbConnect) {
		dbConnect = nouvdbConnect;
	}

	@Override
	public void create() throws Exception {
		CallableStatement cstmt = null;
		try {
			String query1 = "call createCarnet(?,?)";
			String query2 = "select CARNET_SEQ.currval from dual";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			PreparedStatement pstm2 = dbConnect.prepareStatement(query2);
			pstm1.setString(1, titre);
			pstm1.setInt(2, id_user);
			int nl = pstm1.executeUpdate();
			ResultSet rs = pstm2.executeQuery();
			if (rs.next()) {
				int nc = rs.getInt(1);
				id_carnet = nc;
			} else {
				System.out
						.println("erreur lors de l'insertion ,numero de carnet introuvable");
			}
		} catch (Exception e) {
			throw new Exception("Erreur: " + e.getMessage());
		} finally {// effectué dans tous les cas
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
			String query1 = "select * from carnet where id_carnet = ?";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_carnet);
			ResultSet rs = pstm1.executeQuery();
			if (rs.next()) {
				id_carnet = rs.getInt(1);
				titre = rs.getString(2);
				id_user = rs.getInt(3);
			} else {
				id_user = -1;
				System.out.println("Aucun carnet avec cet identifiant.");
			}
		} catch (Exception e) {
			throw new Exception("Erreur: " + e.getMessage());
		} finally {// effectué dans tous les cas
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
			String query1 = "call UpdateCarnet(?,?,?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_carnet);
			pstm1.setString(2, titre);
			pstm1.setInt(3, id_user);
			int nl = pstm1.executeUpdate();
		} catch (Exception e) {
			throw new Exception("Erreur: " + e.getMessage());
		} finally {// effectué dans tous les cas
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
			String query1 = "call deleteCarnet(?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_carnet);
			int nl = pstm1.executeUpdate();
		} catch (Exception e) {
			throw new Exception("Erreur: " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static ArrayList<CarnetDB> getUser(int var) throws Exception {
		ArrayList<CarnetDB> list = new ArrayList();
		CallableStatement cstmt = null;
		try {
			boolean trouve = false;
			String query1 = "select * from carnet where id_user = ?";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, var);
			ResultSet rs = pstm1.executeQuery();
			while (rs.next()) {
				trouve = true;
				list.add(new CarnetDB(rs.getInt(1), rs.getString(2), rs
						.getInt(3), null));
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
