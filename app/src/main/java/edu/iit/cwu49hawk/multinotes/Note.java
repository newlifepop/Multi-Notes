package edu.iit.cwu49hawk.multinotes;

import java.io.Serializable;

/**
 * Created by wsy37 on 3/1/2017.
 */

public class Note implements Serializable {

    private String noteTitle;
    private String noteContent;
    private String noteUpdateTime;

    public Note(String noteTitle, String noteContent, String noteUpdateTime) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteUpdateTime = noteUpdateTime;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public void setNoteUpdateTime(String noteUpdateTime) {
        this.noteUpdateTime = noteUpdateTime;
    }

    public String getNoteTitle() {
        return this.noteTitle;
    }

    public String getNoteContent() {
        return this.noteContent;
    }

    public String getNoteUpdateTime() {
        return this.noteUpdateTime;
    }
}