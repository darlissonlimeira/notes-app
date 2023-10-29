package com.br.notesapp.notesappserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Document(collection = "notes")
public class Note implements Serializable {

    @Id
    private String id;

    @DocumentReference(lazy = true)
    private UserModel user;

    private String title;

    private String text;

    private boolean completed = false;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    public Note() {
    }

    public Note(UserModel user, String title, String text) {
        this.user = user;
        this.title = title;
        this.text = text;
    }

    public Note(String id, UserModel user, String title, String text, boolean completed, Date createdAt, Date updatedAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.text = text;
        this.completed = completed;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(getId(), note.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
