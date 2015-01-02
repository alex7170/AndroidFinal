package com.example.projetgroupe;

import java.sql.Connection;
import java.util.ArrayList;

import modele.CarnetDB;
import modele.NoteDB;
import modele.UserDB;
import myconnections.DBConnection;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CarnetFragment extends Fragment {
	Connection con = null;
	ListView list_carnet;
	ArrayList<CarnetDB> list_carnet_obj = null;
	ArrayList<String> list_carnet_titre = null;
	AlertDialog alert = null;
	AjoutCarnetDB acDB = null;
	EditNomCarnetDB encDB = null;
	GetListCarnetDB glcDB = null;
	DeleteCarnetDB dcDB = null;
	ArrayAdapter<String> adapter = null;
	Integer pos;
	UserDB tmpUser;

	public CarnetFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_carnet, container,
				false);

		list_carnet_titre = new ArrayList<String>();
		list_carnet_obj = new ArrayList<CarnetDB>();

		list_carnet = (ListView) rootView.findViewById(R.id.list_carnet);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				list_carnet_titre) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				TextView text = (TextView) view
						.findViewById(android.R.id.text1);
				if (list_carnet_obj.size() < 5 && position == 0) {
					text.setTextColor(Color.GRAY);
				} else {
					text.setTextColor(Color.BLACK);
				}
				return view;
			}
		};
		list_carnet.setAdapter(adapter);

		list_carnet
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {
						pos = i;
						if (list_carnet_obj.size() < 5) {
							if (i == 0) {
								final EditText nouveauCarnet = new EditText(
										getActivity());
								nouveauCarnet.setHint(getResources().getString(R.string.pdg_car_addNom));

								alert = new AlertDialog.Builder(getActivity())
										.setTitle(getResources().getString(R.string.pdg_car_addTitle))
										.setMessage(
												getResources().getString(R.string.pdg_car_addContainer))
										.setView(nouveauCarnet)
										.setPositiveButton(
												getResources().getString(R.string.pdg_car_addYes),
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {

														acDB = new AjoutCarnetDB(
																(ActivityPrincipale) getActivity(),
																nouveauCarnet
																		.getText()
																		.toString());
														acDB.execute();
													}
												})
										.setNegativeButton(
												getResources().getString(R.string.pdg_car_addNo),
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int whichButton) {
													}
												}).show();

							} else {
								AlertDialog.Builder builder = new AlertDialog.Builder(
										getActivity());
								builder.setTitle(getResources().getString(R.string.pdg_car_optionTitle));
								builder.setItems(new CharSequence[] { getResources().getString(R.string.pdg_car_optionLire),
										getResources().getString(R.string.pdg_car_optionModifier), getResources().getString(R.string.pdg_car_optionSupprimer), getResources().getString(R.string.pdg_car_optionCancel) },
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												switch (which) {
												case 0:
													Intent carnetIntent = new Intent(
															getActivity(),
															ActivityLireCarnet.class);
													carnetIntent
															.putExtra(
																	"carnet",
																	(CarnetDB) list_carnet_obj
																			.get(pos - 1));
													startActivityForResult(
															carnetIntent, 0);
													break;
												case 1:
													final EditText nouveauCarnet = new EditText(
															getActivity());
													nouveauCarnet
															.setHint(getResources().getString(R.string.pdg_car_addNom));

													alert = new AlertDialog.Builder(
															getActivity())
															.setTitle(
																	getResources().getString(R.string.pdg_car_editTitle)
																			+ list_carnet_obj
																					.get(pos - 1)
																					.getTitre())
															.setMessage(
																	getResources().getString(R.string.pdg_car_editContainer))
															.setView(
																	nouveauCarnet)
															.setPositiveButton(
																	getResources().getString(R.string.pdg_car_editYes),
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int whichButton) {

																			encDB = new EditNomCarnetDB(
																					(ActivityPrincipale) getActivity(),
																					list_carnet_obj
																							.get(pos - 1),
																					nouveauCarnet
																							.getText()
																							.toString());
																			encDB.execute();
																		}
																	})
															.setNegativeButton(
																	getResources().getString(R.string.pdg_car_editNo),
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int whichButton) {
																		}
																	}).show();
													break;
												case 2:
													AlertDialog.Builder builder = new AlertDialog.Builder(
															getActivity());
													builder.setTitle(
															getResources().getString(R.string.pdg_car_editTitle)
																	+ list_carnet_obj
																			.get(pos - 1)
																			.getTitre())
															.setMessage(getResources().getString(R.string.pdg_car_deleteContainer))
															.setPositiveButton(
																	getResources().getString(R.string.pdg_car_deleteConfirm),
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int id) {
																			dcDB = new DeleteCarnetDB(
																					(ActivityPrincipale) getActivity(),
																					list_carnet_obj
																							.get(pos - 1));
																			dcDB.execute();
																		}
																	})
															.setNegativeButton(
																	getResources().getString(R.string.pdg_car_editNo),
																	new DialogInterface.OnClickListener() {
																		public void onClick(
																				DialogInterface dialog,
																				int id) {
																		}
																	}).show();
													break;
												default:
													break;
												}
											}
										});
								builder.create().show();

							}
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							builder.setTitle("Option");
							builder.setItems(new CharSequence[] { getResources().getString(R.string.pdg_car_optionLire),
									getResources().getString(R.string.pdg_car_optionModifier), getResources().getString(R.string.pdg_car_optionSupprimer), getResources().getString(R.string.pdg_car_optionCancel) },
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											switch (which) {
											case 0:
												Intent carnetIntent = new Intent(
														getActivity(),
														ActivityLireCarnet.class);
												carnetIntent
														.putExtra(
																"carnet",
																(CarnetDB) list_carnet_obj
																		.get(pos));
												startActivityForResult(
														carnetIntent, 0);
												break;
											case 1:
												final EditText nouveauCarnet = new EditText(
														getActivity());
												nouveauCarnet
														.setHint(getResources().getString(R.string.pdg_car_addNom));

												alert = new AlertDialog.Builder(
														getActivity())
														.setTitle(
																getResources().getString(R.string.pdg_car_editTitle)
																		+ list_carnet_obj
																				.get(pos)
																				.getTitre())
														.setMessage(
																getResources().getString(R.string.pdg_car_editContainer))
														.setView(nouveauCarnet)
														.setPositiveButton(
																getResources().getString(R.string.pdg_car_editYes),
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int whichButton) {
																		encDB = new EditNomCarnetDB(
																				(ActivityPrincipale) getActivity(),
																				list_carnet_obj
																						.get(pos),
																				nouveauCarnet
																						.getText()
																						.toString());
																		encDB.execute();
																	}
																})
														.setNegativeButton(
																getResources().getString(R.string.pdg_car_editNo),
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int whichButton) {
																	}
																}).show();
												break;
											case 2:
												AlertDialog.Builder builder = new AlertDialog.Builder(
														getActivity());
												builder.setTitle(
														getResources().getString(R.string.pdg_car_editTitle)
																+ list_carnet_obj
																		.get(pos)
																		.getTitre())
														.setMessage(
																getResources().getString(R.string.pdg_car_deleteContainer))
														.setPositiveButton(
																getResources().getString(R.string.pdg_car_deleteConfirm),
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {
																		dcDB = new DeleteCarnetDB(
																				(ActivityPrincipale) getActivity(),
																				list_carnet_obj
																						.get(pos));
																		dcDB.execute();
																	}
																})
														.setNegativeButton(
																getResources().getString(R.string.pdg_car_editNo),
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {
																	}
																}).show();
												break;
											default:
												break;
											}
										}

									});
							builder.create().show();
						}
					}
				});

		refreshData();
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 20) {
			glcDB = new GetListCarnetDB((ActivityPrincipale) getActivity());
			glcDB.execute();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class DeleteCarnetDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ArrayList<NoteDB> list2 = null;
		CarnetDB carnet;

		public DeleteCarnetDB(ActivityPrincipale activityPrincipale,
				CarnetDB obj) {
			this.carnet = obj;

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
				try {
					CarnetDB.setConnection(con);
					carnet.delete();
					resultat = getResources().getString(R.string.pdg_car_deleteSucces);
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
				glcDB = new GetListCarnetDB((ActivityPrincipale) getActivity());
				glcDB.execute();
			}
			Toast.makeText(getActivity().getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();

		}

	}

	class EditNomCarnetDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ArrayList<NoteDB> list2 = null;
		CarnetDB carnet;
		String nouveauNomCarnet;

		public EditNomCarnetDB(ActivityPrincipale activityPrincipale,
				CarnetDB obj, String titre) {
			this.nouveauNomCarnet = titre;
			this.carnet = obj;

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
				CarnetDB.setConnection(con);
				try {
					carnet.setTitre(nouveauNomCarnet);
					carnet.update();
					resultat = getResources().getString(R.string.pdg_car_editSucces);
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
				glcDB = new GetListCarnetDB((ActivityPrincipale) getActivity());
				glcDB.execute();
			}
			Toast.makeText(getActivity().getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();

		}

	}

	class AjoutCarnetDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		String varTmp;

		public AjoutCarnetDB(ActivityPrincipale activityPrincipale, String titre) {
			varTmp = titre;
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
				try {
					CarnetDB.setConnection(con);
					UserDB o = (UserDB) getActivity().getIntent()
							.getSerializableExtra("user");
					if (!varTmp.isEmpty()) {
						CarnetDB carnet = new CarnetDB(varTmp, o.getId_user());
						carnet.create();
						ok = true;
					} else {
						resultat = getResources().getString(R.string.pdg_car_empty);
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
				glcDB = new GetListCarnetDB((ActivityPrincipale) getActivity());
				glcDB.execute();
			} else {
				Toast.makeText(getActivity().getApplicationContext(), resultat,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class GetListCarnetDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		private ArrayList<CarnetDB> list_carnet = null;
		ArrayList<NoteDB> list2 = null;

		public GetListCarnetDB(ActivityPrincipale activityPrincipale) {

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
				try {
					CarnetDB.setConnection(con);
					NoteDB.setConnection(con);

					UserDB o = (UserDB) getActivity().getIntent()
							.getSerializableExtra("user");

					list_carnet = new ArrayList<CarnetDB>();
					list_carnet = CarnetDB.getUser(o.getId_user());
					o.setListCarnet(list_carnet);
					for (CarnetDB obj : o.getListCarnet()) {
						list2 = NoteDB.getCarnet(obj.getId_carnet());
						obj.setListNote(list2);
					}
					getActivity().getIntent().putExtra("user", o);
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
			} else {
				Toast.makeText(getActivity().getApplicationContext(), resultat,
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	protected void refreshData() {
		list_carnet_titre.clear();
		UserDB o = (UserDB) getActivity().getIntent().getSerializableExtra(
				"user");
		list_carnet_obj = o.getListCarnet();

		if (list_carnet_obj.size() < 5) {
			list_carnet_titre.add(getResources().getString(R.string.pdg_car_listview_1)
					+ list_carnet_obj.size() + getResources().getString(R.string.pdg_car_listview_2));
		}
		for (int i = 0; i < 5; i++) {
			if (i < list_carnet_obj.size()) {
				list_carnet_titre.add(list_carnet_obj.get(i).getTitre());
			}
		}

		adapter.notifyDataSetChanged();
	}

}