package com.example.projetgroupe;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import modele.CarnetDB;
import modele.CategorieDB;
import modele.NoteDB;
import myconnections.DBConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityAddNote extends Activity {

	ArrayList<CategorieDB> listCat = new ArrayList<CategorieDB>();
	ArrayList<String> listCatTitre = new ArrayList<String>();
	Connection con = null;
	GetCategorieDB gcDB;
	AddNoteDB anDB;
	Spinner spinner_categorie;
	EditText titre, contenu;
	String titreTexte, contenuTexte;
	CarnetDB carnet;
	ArrayAdapter<String> adapter_state;
	Integer categoriePos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_note);
		setTitle(getResources().getString(R.string.label_add_title));
		getActionBar().setIcon(R.drawable.blocknote_document);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		gcDB = new GetCategorieDB(ActivityAddNote.this);
		gcDB.execute();

		carnet = (CarnetDB) getIntent().getSerializableExtra("carnet");
		adapter_state = new ArrayAdapter<String>(this, R.drawable.spinner_item,
				listCatTitre);
		spinner_categorie = (Spinner) findViewById(R.id.note_add_categorie);
		spinner_categorie.setAdapter(adapter_state);
		spinner_categorie
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {
						categoriePos = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parentView) {
					}

				});
		titre = (EditText) findViewById(R.id.note_add_titre);
		contenu = (EditText) findViewById(R.id.note_add_contenu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			setResult(0);
			finish();
		}
		if (id == R.id.action_note_add_send) {
			anDB = new AddNoteDB(ActivityAddNote.this);
			anDB.execute();
		}
		return super.onOptionsItemSelected(item);
	}

	class AddNoteDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;

		public AddNoteDB(ActivityAddNote activityAddNote) {
			link(activityAddNote);
		}

		private void link(ActivityAddNote activityAddNote) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityAddNote.this);
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
					NoteDB.setConnection(con);

					titreTexte = titre.getText().toString();
					contenuTexte = contenu.getText().toString();
					if (!titreTexte.isEmpty() && !contenuTexte.isEmpty()) {
						java.util.Calendar cal = java.util.Calendar
								.getInstance();
						java.util.Date utilDate = cal.getTime();
						java.sql.Date sqlDate = new Date(utilDate.getTime());
						NoteDB newNote = new NoteDB(titreTexte, contenuTexte,
								sqlDate, carnet.getId_carnet(),
								listCat.get(categoriePos));
						newNote.create();
						resultat = getResources().getString(R.string.label_add_succes);
						ok = true;
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
				setResult(20);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), resultat,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	class GetCategorieDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ArrayList<CategorieDB> listCategorie = new ArrayList<CategorieDB>();

		public GetCategorieDB(ActivityAddNote activityAddNote) {
			link(activityAddNote);
		}

		private void link(ActivityAddNote activityAddNote) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityAddNote.this);
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
					CategorieDB.setConnection(con);

					listCategorie = CategorieDB.getFull();
					ok = true;
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
				listCat = listCategorie;
				refreshData();
			} else {
				Toast.makeText(getApplicationContext(), resultat,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void refreshData() {
		listCatTitre.clear();

		if (listCat.size() > 0) {
			for (int u = 0; u < listCat.size(); u++) {
				listCatTitre.add(listCat.get(u).getLabel());
			}
		}
		adapter_state.notifyDataSetChanged();
	}
}
