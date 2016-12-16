package edu.iastate.pal.templates;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private String owner;
    private String created;
    private String lastUpdated;
    private String title;
    private String content;

    public Note(String owner, String title, String content) {
        setCreationDate();
        setOwner(owner);
        setTitle(title);
        setContent(content);
    }

    @SuppressLint("SimpleDateFormat")
    private void setCreationDate() {
        this.created = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
        setLastUpdated();
    }

    private void setOwner(String user) {
        this.owner = user;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    @SuppressLint("SimpleDateFormat")
    public void setLastUpdated() {
        this.lastUpdated = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }
}
