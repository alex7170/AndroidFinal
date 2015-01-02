package com.example.projetgroupe;

import java.sql.Connection;
import java.util.ArrayList;

import modele.CarnetDB;
import modele.NoteDB;
import modele.UserDB;
import myconnections.DBConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Connection con = null;

	private Button button1 = null, button2 = null;
	private EditText log, mdp;
	private String logTmp, mdpTmp;
	UserDB u = null;

	public final static int CHOOSE_BUTTON_REQUEST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		button1 = (Button) findViewById(R.id.btn_login);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAccesDB adb = new MyAccesDB(MainActivity.this);
				adb.execute();
			}
		});
		button2 = (Button) findViewById(R.id.btn_register);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent inscriptionIndent = new Intent(MainActivity.this,
						ActivityInscription.class);
				startActivity(inscriptionIndent);
			}
		});
	}

	class MyAccesDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ArrayList<CarnetDB> list = null;
		ArrayList<NoteDB> list2 = null;

		public MyAccesDB(MainActivity pActivity) {
			link(pActivity);
		}

		private void link(MainActivity pActivity) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(MainActivity.this);
			pgd.setMessage(getResources().getString(R.string.pdg_gen_load));
			pgd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pgd.show();

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if (con == null) {
				con = new DBConnection().getConnection();
			}
			if (con == null) {
				resultat = getResources().getString(R.string.pdg_gen_confail);
				return false;
			} else {
				try {
					UserDB.setConnection(con);
					CarnetDB.setConnection(con);
					NoteDB.setConnection(con);

					log = (EditText) findViewById(R.id.mail);
					mdp = (EditText) findViewById(R.id.password);
					logTmp = log.getText().toString();
					mdpTmp = mdp.getText().toString();

					if (!logTmp.isEmpty()) {
						if (!mdpTmp.isEmpty()) {
							u = new UserDB(logTmp, mdpTmp);
							if (u.checkLogin()) {
								ok = true;
								list = CarnetDB.getUser(u.getId_user());
								u.setListCarnet(list);
								for (CarnetDB obj : u.getListCarnet()) {
									list2 = NoteDB
											.getCarnet(obj.getId_carnet());
									obj.setListNote(list2);
								}
								resultat = getResources().getString(R.string.pdg_co_loginSucces);
								ok = true;

							} else {
								resultat = getResources().getString(R.string.pdg_con_fail);
							}

						} else {
							ok = false;
							resultat = getResources().getString(R.string.pgd_ins_fail_mdp);
						}
					} else {
						ok = false;
						resultat = getResources().getString(R.string.pgd_ins_fail_mail);
					}
				} catch (Exception e) {
					resultat = e.getMessage();
				}
			}
			return ok;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			pgd.dismiss();
			if (ok) {
				pgd.dismiss();
				Intent accueilIndent = new Intent(MainActivity.this,
						ActivityPrincipale.class);
				accueilIndent.putExtra("user", (UserDB) u);
				startActivity(accueilIndent);
			}
			Toast.makeText(getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();

		}

	}

}