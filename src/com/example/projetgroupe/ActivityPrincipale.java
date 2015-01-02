package com.example.projetgroupe;

import java.sql.Connection;
import java.util.ArrayList;

import modele.CarnetDB;
import modele.NavDrawerItem;
import modele.NoteDB;
import modele.UserDB;
import myconnections.DBConnection;
import adapter.NavDrawerListAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityPrincipale extends Activity {
	final String UserID = "";
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;

	private CharSequence mTitle;

	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	UserDB utilisateur = null;
	private Connection con = null;
	private ActionRefreshData ardDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setIcon(R.drawable.blocknote_home);
		utilisateur = (UserDB) getIntent().getSerializableExtra("user");

		mTitle = mDrawerTitle = getTitle();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// Accueil
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Carnet
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Mon Compte
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// A propos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Deconnexion
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));

		navMenuIcons.recycle();
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(
						getResources().getString(R.string.menu_drawner));
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayView(0);
		}
	}

	private class SlideMenuClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			displayView(arg2);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_resfresh_data:
			ardDB = new ActionRefreshData(ActivityPrincipale.this);
			ardDB.execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);

	}

	private void displayView(int position) {
		boolean logout = false;
		Fragment fragment = null;
		switch (position) {
		case 0:// Accueil
			fragment = new AccueilFragment();
			getActionBar().setIcon(R.drawable.blocknote_home);
			break;
		case 1:// Carnet
			fragment = new CarnetFragment();
			getActionBar().setIcon(R.drawable.blocknote_carnet);
			break;
		case 2:// mon compte
			fragment = new MonCompteFragment();
			getActionBar().setIcon(R.drawable.blocknote_client);
			break;
		case 3: // a propos de nous
			fragment = new AboutUsFragment();
			getActionBar().setIcon(R.drawable.blocknote_help);
			break;
		case 4: // deconnexion
			AlertDialog.Builder builder = new AlertDialog.Builder(
					ActivityPrincipale.this);
			builder.setTitle(getResources().getString(R.string.pdg_pri_logoutTitle))
					.setMessage(getResources().getString(R.string.pdg_pri_logoutContainer))
					.setPositiveButton(getResources().getString(R.string.pdg_pri_logoutYes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Toast.makeText(getApplicationContext(),
											getResources().getString(R.string.pdg_pri_logoutOk),
											Toast.LENGTH_SHORT).show();
									finish();
								}
							})
					.setNegativeButton(getResources().getString(R.string.pdg_pri_logoutNo),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							}).show();
			logout = true;
			break;
		default:
			break;
		}
		if (fragment != null) {
			FragmentManager fragmentManage = getFragmentManager();
			fragmentManage.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			if (!logout) {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.pdg_pri_fragmentFailed),
						Toast.LENGTH_SHORT).show();
				logout = false;
			}

		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ActivityPrincipale.this);
		builder.setTitle(getResources().getString(R.string.pdg_pri_logoutTitle))
				.setMessage(getResources().getString(R.string.pdg_pri_logoutContainer))
				.setPositiveButton(getResources().getString(R.string.pdg_pri_logoutYes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Toast.makeText(getApplicationContext(),
										getResources().getString(R.string.pdg_pri_logoutOk),
										Toast.LENGTH_SHORT).show();
								finish();
							}
						})
				.setNegativeButton(getResources().getString(R.string.pdg_pri_logoutNo),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						}).show();
		return;
	}

	class ActionRefreshData extends AsyncTask<String, Integer, Boolean> {
		private String resultat = "";
		private ProgressDialog pgd = null;
		private boolean ok = false;
		ActivityPrincipale act = null;
		ArrayList<CarnetDB> list = null;
		ArrayList<NoteDB> list2 = null;

		public ActionRefreshData(ActivityPrincipale activityPrincipale) {
			act = activityPrincipale;
			link(activityPrincipale);
		}

		private void link(ActivityPrincipale activityPrincipale) {

		}

		protected void onPreExecute() {
			super.onPreExecute();
			pgd = new ProgressDialog(ActivityPrincipale.this);
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
					list = CarnetDB.getUser(utilisateur.getId_user());
					utilisateur.setListCarnet(list);
					for (CarnetDB obj : utilisateur.getListCarnet()) {
						list2 = NoteDB.getCarnet(obj.getId_carnet());
						obj.setListNote(list2);
					}
					resultat = getResources().getString(R.string.pdg_gen_ask_ok);
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

			Toast.makeText(getApplicationContext(), resultat,
					Toast.LENGTH_SHORT).show();
		}
	}

}