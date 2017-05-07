package edu.iit.cwu49hawk.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wsy37 on 3/1/2017.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView noteTitle;
    public TextView noteUpdateTime;
    public TextView noteContent;

    public NoteViewHolder(View view) {
        super(view);
        noteTitle = (TextView) view.findViewById(R.id.noteTitle);
        noteUpdateTime = (TextView) view.findViewById(R.id.noteUpdateTime);
        noteContent = (TextView) view.findViewById(R.id.noteContent);
    }
}