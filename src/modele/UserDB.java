package modele;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDB extends User implements CRUD, Serializable {

	protected static Connection dbConnect = null;

	// ok

	public UserDB(int id_user) {
		super(id_user);
	}

	public UserDB(String mail, String password) {
		super(mail, password);
	}

	public UserDB(String pseudo, String mail, String password) {
		super(pseudo, mail, password);
	}

	public UserDB(int id_user, String pseudo, String mail, String password) {
		super(id_user, pseudo, mail, password);
	}

	public static void setConnection(Connection nouvdbConnect) {
		dbConnect = nouvdbConnect;
	}

	@Override
	public void create() throws Exception {
		CallableStatement cstmt = null;
		try {
			String query1 = "call createUser(?,?,?)";
			String query2 = "select USER_SEQ.currval from dual";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			PreparedStatement pstm2 = dbConnect.prepareStatement(query2);
			pstm1.setString(1, pseudo);
			pstm1.setString(2, mail);
			pstm1.setString(3, password);
			int nl = pstm1.executeUpdate();
			ResultSet rs = pstm2.executeQuery();
			if (rs.next()) {
				int nc = rs.getInt(1);
				id_user = nc;
			} else {
				System.out
						.println("erreur lors de l'insertion ,numero de client introuvable");
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
			String query1 = "select * from users where id_user = ?";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_user);
			ResultSet rs = pstm1.executeQuery();
			if (rs.next()) {
				id_user = rs.getInt(1);
				pseudo = rs.getString(2);
				mail = rs.getString(3);
				password = rs.getString(4);
			} else {
				id_user = -1;
				System.out.println("Aucun utilisateur avec cet identifiant.");
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
			String query1 = "call UpdateUser(?,?,?,?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_user);
			pstm1.setString(2, pseudo);
			pstm1.setString(3, mail);
			pstm1.setString(4, password);
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
			String query1 = "call deleteUsers(?)";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setInt(1, id_user);
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

	public boolean checkLogin() throws Exception {
		boolean a = false;
		CallableStatement cstmt = null;
		try {
			String query1 = "select * from users where mail = ? and password = ?";
			PreparedStatement pstm1 = dbConnect.prepareStatement(query1);
			pstm1.setString(1, mail);
			pstm1.setString(2, password);
			ResultSet rs = pstm1.executeQuery();
			if (rs.next()) {
				id_user = rs.getInt(1);
				pseudo = rs.getString(2);
				mail = rs.getString(3);
				password = rs.getString(4);
				a = true;
			} else {
				id_user = -1;
				System.out
						.println("Aucun utilisateur avec ce pseudo & mot de passe.");
			}
		} catch (Exception e) {
			throw new Exception("Erreur: " + e.getMessage());
		} finally {// effectué dans tous les cas
			try {
				cstmt.close();
			} catch (Exception e) {
			}
		}
		return a;
	}
}
