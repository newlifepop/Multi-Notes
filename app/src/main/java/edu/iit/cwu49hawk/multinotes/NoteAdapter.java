package edu.iit.cwu49hawk.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wsy37 on 3/1/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> noteList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> noteList, MainActivity mainAct) {
        this.noteList = noteList;
        this.mainAct = mainAct;
    }

    public NoteViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.noteTitle.setText(note.getNoteTitle());
        holder.noteUpdateTime.setText(note.getNoteUpdateTime());
        String contentDisplay;
        if(note.getNoteContent().length() > 80) {
            contentDisplay = note.getNoteContent().substring(0, 79) + "...";
        }
        else {
            contentDisplay = note.getNoteContent();
        }
        holder.noteContent.setText(contentDisplay);
    }

    public int getItemCount() {
        return this.noteList.size();
    }
}