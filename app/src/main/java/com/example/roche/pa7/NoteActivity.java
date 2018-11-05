package com.example.roche.pa7;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private final static String TAG = "NoteActivity";

    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final ArrayList<String> titles = (ArrayList) intent.getSerializableExtra(MainActivity.TITLES);

        // Create Note Activity Programmatically
        // Grid Layout to contain Linear Layout, Content EditText, And Done Button
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(1);

        // Linear Layout to contain Title EditText and Category Spinner
        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);

        // Create and set layout parameters for Title EditText
        final EditText titleEditText = new EditText(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.weight = 2;
        titleEditText.setHint(R.string.title);
        titleEditText.setLayoutParams(titleParams);

        // Create and set layout parameters for Category Spinner
        final Spinner categorySpinner = new Spinner(this);
        LinearLayout.LayoutParams categoryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        categoryParams.weight = 1;
        ArrayAdapter<String> categoryArrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.activity_list_item,
                android.R.id.text1,
                getResources().getStringArray(R.array.categories))
        {
            // an anonymous subclass of
            // arrayadapter
            // override getView() and getDropDownView()
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                String category = getItem(position);
                View view =  super.getView(position, convertView, parent);

                ImageView imageView1 = view.findViewById(android.R.id.icon);
//
                switch (category){

                    case "School":
                        imageView1.setImageResource(R.drawable.school);
                        break;
                    case "Work":
                        imageView1.setImageResource(R.drawable.work);
                        break;
                    case "Other":
                        imageView1.setImageResource(R.drawable.other);
                        break;
                    case "Personal":
                        imageView1.setImageResource(R.drawable.personal);
                        break;
                    default:
                        return view;

                }
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view =  super.getView(position, convertView, parent);

                String category = getItem(position);
                ImageView imageView1 = view.findViewById(android.R.id.icon);

                switch (category){

                    case "School":
                        imageView1.setImageResource(R.drawable.school);
                        break;
                    case "Work":
                        imageView1.setImageResource(R.drawable.work);
                        break;
                    case "Other":
                        imageView1.setImageResource(R.drawable.other);
                        break;
                    case "Personal":
                        imageView1.setImageResource(R.drawable.personal);
                        break;
                    default:
                        return view;

                }
                return view;
            }


        };

        categorySpinner.setAdapter(categoryArrayAdapter);
        categorySpinner.setLayoutParams(categoryParams);

        // Create and set layout parameters for Content EditText
        final EditText contentEditText = new EditText(this);
        GridLayout.LayoutParams contentParams = new GridLayout.LayoutParams();
        contentParams.setGravity(Gravity.FILL);
        contentEditText.setLayoutParams(contentParams);

        // Create and set layout parameters for Done Button
        final Button doneButton = new Button(this);
        GridLayout.LayoutParams doneButtonParams = new GridLayout.LayoutParams();
        doneButtonParams.setGravity(Gravity.FILL_HORIZONTAL);
        doneButton.setLayoutParams(doneButtonParams);
        doneButton.setText(getString(R.string.done));
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(titleEditText.getText().length() > 0 && !titles.contains(titleEditText.getText().toString())){
                    Intent intent = new Intent();

                    note.setTitle(titleEditText.getText().toString());
                    note.setCategory(categorySpinner.getSelectedItem().toString());
                    note.setContent(contentEditText.getText().toString());
                    switch(note.getCategory()){

                        case "Personal":
                            note.setImageResource(R.drawable.personal);
                            break;
                        case "Work":
                            note.setImageResource(R.drawable.work);
                            break;
                        case "School":
                            note.setImageResource(R.drawable.school);
                            break;
                        case "Other":
                            note.setImageResource(R.drawable.other);
                            break;
                        default:
                            note.setImageResource(-1);
                    }
                    intent.putExtra(MainActivity.NOTE, note);
                    setResult(Activity.RESULT_OK, intent);
                    NoteActivity.this.finish();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.valid_title), Toast.LENGTH_LONG).show();
                }
            }
        });

        // Add Title EditText and Category Spinner to Linear Layout
        linearLayout.addView(titleEditText);
        linearLayout.addView(categorySpinner);

        // Add LinearLayout, Content EditText, and Done Button to GridLayout
        gridLayout.addView(linearLayout);
        gridLayout.addView(contentEditText);
        gridLayout.addView(doneButton);
        setContentView(gridLayout);

        note = new Note();
        // Sets Title, Category, and Content if the note is being edited
        if(intent.getBooleanExtra(MainActivity.NEW, false)){
            note = (Note) intent.getSerializableExtra(MainActivity.NOTE);
            String categories[] = getResources().getStringArray(R.array.categories);
            titleEditText.setText(note.getTitle());
            categorySpinner.setSelection(Arrays.asList(categories).indexOf(note.getCategory()));
            contentEditText.setText(note.getContent());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                NoteActivity.this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}