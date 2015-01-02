package com.example.projetgroupe;

import java.sql.Connection;

import modele.UserDB;
import myconnections.DBConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityInscription extends Activity {

	private Connection con = null;
	UserDB u = null;
	private EditText pseudo, log, mdp;
	private String logTmp, mdpTmp, pseudoTmp;
	private Button btn_register_sub = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		btn_register_sub = (Button) findViewById(R.id.btn_register_sub);
		btn_register_sub.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyAccesInscriptionDB adb = new MyAccesInscriptionDB(
						ActivityInscription.this);
				adb.execute();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	class MyAccesInscriptionDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ActivityInscription activityParent = null;

		public MyAccesInscriptionDB(ActivityInscription activityInscription) {
			activityParent = activityInscription;
		}

		public MyAccesInscriptionDB(MainActivity pActivity) {
			link(pActivity);
		}

		private void link(MainActivity pActivity) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityInscription.this);
			pgd.setMessage(getResources().getString(R.string.pgd_ins_load));
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

					log = (EditText) findViewById(R.id.mail);
					mdp = (EditText) findViewById(R.id.password);
					pseudo = (EditText) findViewById(R.id.Pseudo);

					logTmp = log.getText().toString();
					mdpTmp = mdp.getText().toString();
					pseudoTmp = pseudo.getText().toString();

					String email = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,})$";

					if (!pseudoTmp.isEmpty()) {
						if (!logTmp.isEmpty()) {
							if (logTmp.matches(email)) {
								if (!mdpTmp.isEmpty()) {
									u = new UserDB(pseudoTmp, logTmp, mdpTmp);
									u.create();
									resultat = getResources().getString(R.string.pgd_ins_win);
									ok = true;

								} else {
									resultat = getResources().getString(
											R.string.pgd_ins_fail_mdp);
								}
							} else {
								resultat = getResources().getString(
										R.string.pgd_ins_fail_mail);
							}

						} else {
							resultat = getResources().getString(
									R.string.pgd_ins_empty_mail);
						}
					} else {
						resultat = getResources().getString(
								R.string.pgd_ins_empty_pass);
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
				activityParent.finish();
			}
			Toast.makeText(getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();

		}

	}

}
