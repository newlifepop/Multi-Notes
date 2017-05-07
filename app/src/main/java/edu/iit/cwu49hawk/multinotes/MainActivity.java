package edu.iit.cwu49hawk.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private static final int A_REQ = 1;
    private static final int E_REQ = 2;
    private Note currentNote;
    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new NoteAdapter(noteList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new AsyncLoad().execute(getString(R.string.file_name));
    }

    private class AsyncLoad extends AsyncTask<String, Note, List<Note>> {

        protected List<Note> doInBackground(String... params) {
            String fileName = params[0];

            try {
                InputStream is = getApplicationContext().openFileInput(fileName);
                JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));

                reader.beginArray();
                while(reader.hasNext()) {
                    Note note = loadNote(reader);
                    publishProgress(note);
                }
                reader.endArray();
                reader.close();
            }
            catch(FileNotFoundException e) {
                e.printStackTrace();
            }
            catch(IOException e) {
                e.printStackTrace();
            }

            return noteList;
        }

        protected void onProgressUpdate(Note... loadedNotes) {
            Note loadedNote = loadedNotes[0];
            noteList.add(loadedNote);
        }

        protected void onPostExecute(List<Note> loadedNoteList) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private Note loadNote(JsonReader reader) {
        String noteTitle = null, noteContent = null, noteUpdateTime = null;
        try {
            reader.beginObject();
            while(reader.hasNext()) {
                String name = reader.nextName();
                if(name.equals("note title")) {
                    noteTitle = reader.nextString();
                }
                else if(name.equals("note content")) {
                    noteContent = reader.nextString();
                }
                else if(name.equals("note update time")) {
                    noteUpdateTime = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        if(noteTitle == null || noteContent == null || noteUpdateTime == null) {
            return null;
        }

        return new Note(noteTitle, noteContent, noteUpdateTime);
    }

    protected void onPause() {
        super.onPause();
        saveFile();
    }

    private void saveFile() {
        try {
            FileOutputStream fos = getApplicationContext()
                    .openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();

            for(Note n: noteList)
                saveNote(writer, n);

            writer.endArray();
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNote(JsonWriter writer, Note note) {
        try {
            writer.beginObject();
            writer.name("note title").value(note.getNoteTitle());
            writer.name("note content").value(note.getNoteContent());
            writer.name("note update time").value(note.getNoteUpdateTime());
            writer.endObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.infoButton:
                Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoIntent);
                return true;

            case R.id.addButton:
                Intent newNoteIntent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(newNoteIntent, A_REQ);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        currentNote = noteList.get(pos);
        Intent modifyNoteIntent = new Intent(MainActivity.this, EditActivity.class);
        modifyNoteIntent.putExtra("CURRENT_NOTE", currentNote);
        startActivityForResult(modifyNoteIntent, E_REQ);
    }

    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        currentNote = noteList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete note \'" + currentNote.getNoteTitle() + "\' ?");
        builder.setIcon(R.drawable.ic_warning_black_24px);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               noteList.remove(currentNote);
               currentNote = null;
               mAdapter.notifyDataSetChanged();

               Toast.makeText(MainActivity.this, "Note removed", Toast.LENGTH_SHORT).show();
           }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {

           }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == E_REQ) {
            if(resultCode == RESULT_OK) {
                Note updatedNote = (Note) data.getSerializableExtra("UPDATED_NOTE");
                noteList.remove(currentNote);
                currentNote = updatedNote;
                noteList.add(0, currentNote);
                mAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == A_REQ) {
            if(resultCode == RESULT_OK) {
                Note newNote = (Note) data.getSerializableExtra("NEW_NOTE");
                currentNote = newNote;
                noteList.add(0, newNote);
                mAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            }
        }
    }
}