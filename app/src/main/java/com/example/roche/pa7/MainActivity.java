package com.example.roche.pa7;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.roche.pa7.NoteDatabaseHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int NEW_NOTE_CODE = 1;
    private static final int EDIT_NOTE_CODE = 2;

    static final String NOTE = "note";
    static final String TITLES = "titles";
    static final String NEW = "new";

    NoteDatabaseHelper noteDatabaseHelper;
    SimpleCursorAdapter cursorAdapter;
    ListView noteListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // database
        noteDatabaseHelper = new NoteDatabaseHelper(this);
        cursorAdapter = new SimpleCursorAdapter(
                this, R.layout.activity_list_item,
                noteDatabaseHelper.getSelectAllNotesCursor(),
                new String[] {NoteDatabaseHelper.TITLE, NoteDatabaseHelper.IMAGE_RESOURCE},
                new int[] {R.id.text1, R.id.icon},
                0){

            @Override
            public void setViewImage(ImageView v, String value) {
                v.setImageResource(Integer.parseInt(value));
            }
        };

        // Grid Layout to contain New Note Button and the Note ListView
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(1);


        // Create and set parameters and action listeners for note list items
        noteListView = new ListView(this);
        noteListView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        noteListView.setAdapter(cursorAdapter);
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Gets note from SQLiteDatabase
                Note note = noteDatabaseHelper.getNoteById(l);
                // add information to intent
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(NEW, true);
                intent.putExtra(TITLES, noteDatabaseHelper.getTitles(note.getId()));
                intent.putExtra(NOTE, note);

                // Starts Note Activity
                startActivityForResult(intent, EDIT_NOTE_CODE);
            }
        });
        noteListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        noteListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            // When an item in noteListView is checked or unchecked
            // the row id is added or removed accordingly from
            // the ids ArrayList. There's probably a better way to do this...
            // Like a function call that just returns an array of all the
            // selected view's ids...
            private ArrayList<Long> ids = new ArrayList<>();

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                // updates contextual action menu
                int numSelected = noteListView.getCheckedItemCount();
                actionMode.setTitle(numSelected + getString(R.string.selected));

                if(ids.contains(l))
                    ids.remove(l);
                else
                    ids.add(l);
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = getMenuInflater();
                menuInflater.inflate(R.menu.cam_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.delete_notes_menu_item:
                        // delete row from SQLite Database and update the Cursor
                        noteDatabaseHelper.deleteNotesByIds(ids);
                        cursorAdapter.changeCursor(noteDatabaseHelper.getSelectAllNotesCursor());
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        // add New Note Button and Note ListView to GridLayout
        gridLayout.addView(noteListView);
        setContentView(gridLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.new_note_menu_item:
                // Adds information to intent
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                intent.putExtra(TITLES, noteDatabaseHelper.getTitles());

                // Starts Note Activity
                startActivityForResult(intent, NEW_NOTE_CODE);
                return true;
            case R.id.delete_all_notes_menu_item:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.are_you_sure))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // deletes all notes in the SQLite Database and updates the Cursor
                                noteDatabaseHelper.deleteAllNotes();
                                cursorAdapter.changeCursor(noteDatabaseHelper.getSelectAllNotesCursor());
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null);
                alertDialog.show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    /**
     * After completing a note, insert/update data in database,
     * and update list view
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){

            if(requestCode == NEW_NOTE_CODE){
                // Inserts note into the SQLite Database
                noteDatabaseHelper.insertNote((Note) data.getSerializableExtra(NOTE));

            } else if(requestCode == EDIT_NOTE_CODE){
                // Updates the note in the Database
                Note note = (Note) data.getSerializableExtra(NOTE);
                noteDatabaseHelper.upDateNoteById(note.getId(), note);
            }

            // Updates list view
            cursorAdapter.changeCursor(noteDatabaseHelper.getSelectAllNotesCursor());
        }
    }
}