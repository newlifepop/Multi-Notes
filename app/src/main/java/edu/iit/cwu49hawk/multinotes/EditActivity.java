package edu.iit.cwu49hawk.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wsy37 on 3/1/2017.
 */

public class EditActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editContent;
    private Note currentNote;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);
        currentNote = new Note(null, null, null);

        Intent intent = getIntent();
        if(intent.hasExtra("CURRENT_NOTE")) {
            currentNote = (Note) intent.getSerializableExtra("CURRENT_NOTE");
            editTitle.setText(currentNote.getNoteTitle());
            editContent.setText(currentNote.getNoteContent());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.saveButton:
                if(editTitle.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Can't save un-titled note", Toast.LENGTH_SHORT).show();

                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
                else if(editTitle.getText().toString().equals(currentNote.getNoteTitle()) &&
                        editContent.getText().toString().equals(currentNote.getNoteContent())) {
                    Intent data = new Intent();
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
                else {
                    Note updatedNote = new Note(editTitle.getText().toString(),
                            editContent.getText().toString(), getCurrentTime());
                    Intent data = new Intent();
                    data.putExtra((currentNote.getNoteTitle() == null?"NEW_NOTE":"UPDATED_NOTE"), updatedNote);
                    setResult(RESULT_OK, data);

                    finish();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if(editTitle.getText().toString().trim().isEmpty() ||
                (editTitle.getText().toString().equals(currentNote.getNoteTitle()) &&
                        editContent.getText().toString().equals(currentNote.getNoteContent()))) {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save note \'" + editTitle.getText().toString() + "\' ?");
            builder.setIcon(R.drawable.ic_warning_black_24px);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   Note updatedNote = new Note(editTitle.getText().toString(),
                           editContent.getText().toString(), getCurrentTime());
                   Intent data = new Intent();
                   data.putExtra((currentNote.getNoteTitle() == null?"NEW_NOTE":"UPDATED_NOTE"), updatedNote);
                   setResult(RESULT_OK, data);

                   finish();
               }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                   Intent data = new Intent();
                   setResult(RESULT_CANCELED, data);
                   finish();
               }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public String getCurrentTime() {
        DateFormat df = new SimpleDateFormat("E MMM d, h:mm a");
        return df.format(new Date()).toString();
    }
}