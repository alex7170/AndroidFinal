package com.example.projetgroupe;

import java.sql.Connection;
import java.util.ArrayList;

import modele.CategorieDB;
import modele.NoteDB;
import myconnections.DBConnection;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class ActivityEditNote extends Activity {
	protected ArrayList<CategorieDB> listCat = new ArrayList<CategorieDB>();
	protected ArrayList<String> listCatTitre = new ArrayList<String>();
	protected Spinner spinner_categorie;
	EditText note_contenu, note_titre;
	ArrayAdapter<String> adapter_state;
	Integer categoriePos = 0;
	Connection con = null;
	EditNoteDB enDB;
	GetCategorieDB gcDB;
	RemoveNoteDB rnDB;
	NoteDB note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.blocknote_document);
		gcDB = new GetCategorieDB(ActivityEditNote.this);
		gcDB.execute();
		adapter_state = new ArrayAdapter<String>(this, R.drawable.spinner_item,
				listCatTitre);
		spinner_categorie = (Spinner) findViewById(R.id.note_edit_categorie);
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
		note = (NoteDB) getIntent().getSerializableExtra("note");
		setTitle(note.getTitre());
		note_titre = (EditText) findViewById(R.id.note_edit_titre);
		note_titre.setText(note.getTitre());
		note_contenu = (EditText) findViewById(R.id.note_edit_contenu);
		note_contenu.setText(note.getContenu());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_edit_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			setResult(0);
			finish();
		}
		if (id == R.id.action_note_edit_send) {
			enDB = new EditNoteDB(ActivityEditNote.this);
			enDB.execute();
		}
		if (id == R.id.action_note_edit_remove) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ActivityEditNote.this);
			builder.setTitle(getResources().getString(R.string.pdg_editnote_title))
					.setMessage(getResources().getString(R.string.pdg_editnote_message))
					.setPositiveButton(getResources().getString(R.string.pdg_editnote_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									rnDB = new RemoveNoteDB(
											ActivityEditNote.this);
									rnDB.execute();
								}
							})
					.setNegativeButton(getResources().getString(R.string.pdg_editnote_no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							}).show();

		}
		return super.onOptionsItemSelected(item);
	}

	class EditNoteDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ArrayList<NoteDB> list2 = null;

		public EditNoteDB(ActivityEditNote activityEditNote) {
			link(activityEditNote);
		}

		private void link(ActivityEditNote activityEditNote) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityEditNote.this);
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

					note.setTitre(note_titre.getText().toString());
					note.setContenu(note_contenu.getText().toString());
					note.setCategorie(listCat.get(categoriePos));
					note.update();
					resultat = getResources().getString(R.string.pdg_mon_ask_editSucces);
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
				setResult(22);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), resultat,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	class RemoveNoteDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ArrayList<NoteDB> list2 = null;

		public RemoveNoteDB(ActivityEditNote activityEditNote) {
			link(activityEditNote);
		}

		private void link(ActivityEditNote activityEditNote) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityEditNote.this);
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
					note.delete();
					resultat = "Suppression réussis";
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
				setResult(23);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), resultat,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	class GetCategorieDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private boolean ok = false;
		ArrayList<CategorieDB> listCategorie = new ArrayList<CategorieDB>();

		public GetCategorieDB(ActivityEditNote activityEditNote) {
			link(activityEditNote);
		}

		private void link(ActivityEditNote activityEditNote) {

		}

		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if (con == null) {// premier invocation
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