package model;

import java.util.Date;
import java.util.Objects;

public class Post {
    private int id;
    private String name;
    private String description;
    private Date created;

    public Post(int id, String name, String description, Date created) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
    }

    public Post(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post Post = (Post) o;
        return id == Post.id &&
                Objects.equals(name, Post.name) &&
                Objects.equals(description, Post.description) &&
                Objects.equals(created, Post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, created);
    }
}
