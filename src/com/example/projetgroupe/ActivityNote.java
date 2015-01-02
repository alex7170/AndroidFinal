package com.example.projetgroupe;

import java.text.SimpleDateFormat;

import modele.NoteDB;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ActivityNote extends Activity {
	NoteDB note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.blocknote_document);
		note = (NoteDB) getIntent().getSerializableExtra("note");
		setTitle(note.getTitre());
		TextView note_contenu, note_date;
		note_contenu = (TextView) findViewById(R.id.note_contenu);
		note_contenu.setMovementMethod(new ScrollingMovementMethod());
		note_contenu.setText(note.getContenu());
		note_date = (TextView) findViewById(R.id.note_date);
		SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yy");
		note_date.setText(formater.format(note.getDate_note()));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_note, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			setResult(0);
			finish();
		}
		if (id == R.id.action_note_edit) {
			Intent noteEditIdent = new Intent(this, ActivityEditNote.class);
			noteEditIdent.putExtra("note", (NoteDB) note);
			startActivityForResult(noteEditIdent, 0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 23 || resultCode == 22) {
			setResult(20);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
