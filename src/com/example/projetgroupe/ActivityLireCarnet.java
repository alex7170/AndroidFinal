package com.example.projetgroupe;

import java.sql.Connection;
import java.util.ArrayList;

import modele.CarnetDB;
import modele.NoteDB;
import myconnections.DBConnection;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLireCarnet extends Activity {

	ArrayList<String> list_titre = null;
	ArrayList<NoteDB> list_note_all = null;
	ArrayAdapter<String> adapter = null;
	private Connection con = null;
	CarnetDB carnet;
	UpdateDB uDB;
	ListView list_carnet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lire_carnet);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		carnet = (CarnetDB) getIntent().getSerializableExtra("carnet");
		setTitle(carnet.getTitre());
		getActionBar().setIcon(R.drawable.blocknote_carnet);

		list_titre = new ArrayList<String>();
		list_note_all = new ArrayList<NoteDB>();

		list_carnet = (ListView) findViewById(R.id.list_carnet_complet);
		adapter = new ArrayAdapter<String>(ActivityLireCarnet.this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				list_titre) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				text.setBackgroundColor(Color.parseColor(list_note_all
						.get(position).getCategorie().getCouleur()));
				return view;
			}
		};
		list_carnet.setAdapter(adapter);
		list_carnet
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
						Intent noteIdent = new Intent(ActivityLireCarnet.this,
								ActivityNote.class);
						noteIdent.putExtra("note",
								(NoteDB) list_note_all.get(i));
						startActivityForResult(noteIdent, 10);
					}
				});
		refreshData();
	}

	private void refreshData() {
		ArrayList<NoteDB> arrayNote;
		NoteDB tmpNote;
		list_titre.clear();
		list_note_all.clear();

		arrayNote = carnet.getListNote();
		if (arrayNote.size() > 0) {
			for (int u = 0; u < arrayNote.size(); u++) {
				tmpNote = arrayNote.get(u);
				list_note_all.add(tmpNote);
				list_titre.add(tmpNote.getTitre());
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_lire_carnet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		}
		if (id == R.id.action_carnet_add) {
			Intent noteIdent = new Intent(ActivityLireCarnet.this,
					ActivityAddNote.class);
			noteIdent.putExtra("carnet", (CarnetDB) carnet);
			startActivityForResult(noteIdent, 10);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 20) {
			uDB = new UpdateDB((ActivityLireCarnet) ActivityLireCarnet.this);
			uDB.execute();
			setResult(20);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public void onBackPressed() {
		finish();
	}


	class UpdateDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		CarnetDB carnetToGet;
		ArrayList<NoteDB> list2 = null;

		public UpdateDB(ActivityLireCarnet activity) {
			link(activity);
		}

		private void link(ActivityLireCarnet activity) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityLireCarnet.this);
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
					CarnetDB.setConnection(con);
					NoteDB.setConnection(con);

					list2 = NoteDB.getCarnet(carnet.getId_carnet());
					carnet.setListNote(list2);
					resultat = getResources().getString(R.string.pdg_gen_ask_ok);
					ok = true;
				} catch (Exception e) {
					e.printStackTrace();
					resultat = e.getMessage();
				}
			}
			return ok;

		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			if (ok) {
				refreshData();
			}
			Toast.makeText(getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();
		}
	}
}
