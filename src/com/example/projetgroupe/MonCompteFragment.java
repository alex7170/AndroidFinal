package com.example.projetgroupe;

import java.sql.Connection;

import modele.UserDB;
import myconnections.DBConnection;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MonCompteFragment extends Fragment {
	private Connection con = null;
	private EditText newPseudo, nouveau = null;
	private EditText nouveauCarnet2 = null;
	private AlertDialog alert = null;
	private EditPseudoDB epDB = null;
	private EditMdpDB epDM = null;
	private DesinscriptionDB acDD = null;
	private UserDB tmpUser = null;

	public MonCompteFragment() {
	}

	public MonCompteFragment(UserDB tmpUser) {
		super();
	}

	private Button btn_mycompte_pseudochange = null,
			btn_mycompte_mdpasse = null, btn_mycompte_des = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View rootView = inflater.inflate(R.layout.fragment_moncompte,
				container, false);
		tmpUser = (UserDB) getActivity().getIntent().getSerializableExtra(
				"user");
		btn_mycompte_pseudochange = (Button) rootView
				.findViewById(R.id.btn_mycompte_pseudochange);
		btn_mycompte_pseudochange
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						newPseudo = new EditText(getActivity());
						newPseudo.setHint(getResources().getString(R.string.pdg_mon_pseudoHint));

						alert = new AlertDialog.Builder(getActivity())
								.setTitle(getResources().getString(R.string.pdg_mon_pseudotitle))
								.setMessage(getResources().getString(R.string.pdg_mon_pseudoMessage))
								.setView(newPseudo)
								.setPositiveButton(getResources().getString(R.string.pdg_mon_Yes),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												epDB = new EditPseudoDB(
														(ActivityPrincipale) getActivity());
												epDB.execute();

											}
										})
								.setNegativeButton(getResources().getString(R.string.pdg_mon_No),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
											}
										}).show();

					}
				});

		btn_mycompte_mdpasse = (Button) rootView
				.findViewById(R.id.btn_mycompte_mdpasse);
		btn_mycompte_mdpasse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nouveau = new EditText(getActivity());
				nouveauCarnet2 = new EditText(getActivity());

				nouveau.setHint(getResources().getString(R.string.pdg_mon_mdpHint));
				nouveauCarnet2.setHint(getResources().getString(R.string.pdg_mon_mdpHint));

				alert = new AlertDialog.Builder(getActivity())
						.setTitle(getResources().getString(R.string.pdg_mon_mdptitle))
						.setMessage(getResources().getString(R.string.pdg_mon_mdpMessage))
						.setView(nouveau)

						.setPositiveButton(getResources().getString(R.string.pdg_mon_Yes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										alert = new AlertDialog.Builder(
												getActivity())
												.setTitle(
														getResources().getString(R.string.pdg_mon_mdptitle))
												.setMessage(
														getResources().getString(R.string.pdg_mon_mdpMessageRe))
												.setView(nouveauCarnet2)

												.setPositiveButton(
														getResources().getString(R.string.pdg_mon_Yes),
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
																epDM = new EditMdpDB(
																		(ActivityPrincipale) getActivity());
																epDM.execute();
															}
														})
												.setNegativeButton(
														getResources().getString(R.string.pdg_mon_No),
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int whichButton) {
															}
														}).show();
									}
								})
						.setNegativeButton(getResources().getString(R.string.pdg_mon_No),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).show();

			}
		});

		btn_mycompte_des = (Button) rootView
				.findViewById(R.id.btn_mycompte_des);
		btn_mycompte_des.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nouveau = new EditText(getActivity());

				alert = new AlertDialog.Builder(getActivity())
						.setTitle(getResources().getString(R.string.pdg_mon_deleteTitle))
						.setMessage(getResources().getString(R.string.pdg_mon_deleteMessage))
						.setPositiveButton(getResources().getString(R.string.pdg_mon_deleteYes),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										acDD = new DesinscriptionDB(
												(ActivityPrincipale) getActivity());
										acDD.execute();

									}
								})
						.setNegativeButton(getResources().getString(R.string.pdg_mon_deleteNo),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
									}
								}).show();

			}
		});

		return rootView;

	}

	class EditPseudoDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ActivityPrincipale act = null;
		String varTmp = newPseudo.getText().toString();

		public EditPseudoDB(ActivityPrincipale activityPrincipale) {
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
			if (con == null) {// premier invocation
				con = new DBConnection().getConnection();
			}
			if (con == null) {
				resultat = getResources().getString(R.string.pdg_gen_load);
				return false;
			} else {
				try {
					UserDB.setConnection(con);
					if (!varTmp.isEmpty()) {
						tmpUser.setPseudo(varTmp);
						tmpUser.update();
						resultat = getResources().getString(R.string.pdg_mon_ask_editSucces);
						ok = true;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					resultat = e.getMessage();
				}
			}
			return ok;
		}

		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pgd.dismiss();
			Toast.makeText(getActivity().getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();

		}
	}

	class EditMdpDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		String varTmp = nouveau.getText().toString();
		String varTmp2 = nouveauCarnet2.getText().toString();

		public EditMdpDB(ActivityPrincipale activityPrincipale) {
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
					UserDB.setConnection(con);
					if (!varTmp.isEmpty()) {
						if (!varTmp2.isEmpty()) {
							if (varTmp.equals(varTmp2)) {
								tmpUser.setPassword(varTmp);
								tmpUser.update();
								resultat = getResources().getString(R.string.pdg_mon_ask_editSucces);
								ok = true;
							} else {
								resultat = getResources().getString(R.string.pdg_mon_ask_mdpFailRe);
							}

						} else {
							resultat = getResources().getString(R.string.pdg_mon_empty);
						}

					} else {
						resultat = getResources().getString(R.string.pdg_mon_empty);
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
			Toast.makeText(getActivity().getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();
		}
	}

	class DesinscriptionDB extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;

		public DesinscriptionDB(ActivityPrincipale activityPrincipale) {
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
					UserDB.setConnection(con);
					tmpUser.delete();
					resultat = getResources().getString(R.string.pdg_mon_unregisterSucces);
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
				getActivity().finish();
			}
			Toast.makeText(getActivity().getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();
		}
	}
}