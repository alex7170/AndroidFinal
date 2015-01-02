package com.example.projetgroupe;

import java.sql.Connection;
import java.util.ArrayList;

import modele.CarnetDB;
import modele.NoteDB;
import modele.UserDB;
import myconnections.DBConnection;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AccueilFragment extends Fragment {

	ArrayList<CarnetDB> list_carnet_obj;
	ArrayList<String> list_titre;
	ArrayList<NoteDB> list_note_all;
	ListView list_carnet;
	ArrayAdapter<String> adapter;
	private Connection con;
	UserDB o;
	UpdateDB uDB;

	public AccueilFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_accueil, container,
				false);
		o = (UserDB) getActivity().getIntent().getSerializableExtra("user");
		list_titre = new ArrayList<String>();
		list_carnet_obj = new ArrayList<CarnetDB>();
		list_note_all = new ArrayList<NoteDB>();
		list_carnet = (ListView) rootView
				.findViewById(R.id.list_carnet_accueil);
		adapter = new ArrayAdapter<String>(getActivity(),
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
						Intent noteIdent = new Intent(getActivity(),
								ActivityNote.class);
						noteIdent.putExtra("note",
								(NoteDB) list_note_all.get(i));
						startActivityForResult(noteIdent, 0);
					}
				});
		refreshData();
		return rootView;
	}

	protected void refreshData() {
		ArrayList<NoteDB> arrayNote;
		CarnetDB tmpCarnet;
		NoteDB tmpNote;
		list_titre.clear();
		list_note_all.clear();

		list_carnet_obj = o.getListCarnet();

		for (int i = 0; i < list_carnet_obj.size(); i++) {
			tmpCarnet = list_carnet_obj.get(i);
			arrayNote = tmpCarnet.getListNote();
			if (arrayNote.size() > 0) {
				for (int u = 0; u < arrayNote.size(); u++) {
					tmpNote = arrayNote.get(u);
					list_note_all.add(tmpNote);
					list_titre.add(tmpNote.getTitre());
				}
			}
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 20) {
			uDB = new UpdateDB((ActivityPrincipale) getActivity());
			uDB.execute();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class UpdateDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ActivityPrincipale act = null;
		ArrayList<CarnetDB> list = null;
		ArrayList<NoteDB> list2 = null;

		public UpdateDB(ActivityPrincipale activityPrincipale) {
			act = activityPrincipale;
			link(activityPrincipale);
		}

		private void link(ActivityPrincipale activityPrincipale) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(getActivity());
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
				UserDB.setConnection(con);
				CarnetDB.setConnection(con);
				NoteDB.setConnection(con);
				try {
					list = CarnetDB.getUser(o.getId_user());
					o.setListCarnet(list);
					for (CarnetDB obj : o.getListCarnet()) {
						list2 = NoteDB.getCarnet(obj.getId_carnet());
						obj.setListNote(list2);
					}
					resultat = getResources()
							.getString(R.string.pdg_gen_ask_ok);
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
				refreshData();
			}
			Toast.makeText(getActivity().getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();
		}
	}
}