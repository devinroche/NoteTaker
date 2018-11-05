package com.example.roche.pa7;

import java.io.Serializable;

public class Note implements Serializable{

    private int id;
    private String title;
    private String content;
    private String category;
    private int imageResource;

    /**
     * Default Value Constructor
     */
    public Note() {
        int id = -1;
        this.title = "Title";
        this.content = "Content";
        this.category = "Category";
        this.imageResource = -1;
    }

    /**
     * Constructor
     * @param title is a String the represents the Note's Title
     * @param content is a String that represents the Note's content
     * @param category is a String that represents the Note's category (ie Type)
     */
    public Note(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    // Setters and Getters


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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @Override
    public String toString() {
        return title;
    }
}