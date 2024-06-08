package com.kehuldroid.noteapp;

public class NoteModel {
    private int id;
    private String title;
    private String content;
    private String timeline;

    private boolean isPinned;


    public NoteModel(){

    }
    public NoteModel(String title, String content,String timeline) {
        this.title = title;
        this.content = content;
        this.timeline=timeline;
    }

    public NoteModel(int id, String title, String content,String timeline) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.timeline=timeline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }
}
