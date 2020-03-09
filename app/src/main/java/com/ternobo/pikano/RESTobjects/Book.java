package com.ternobo.pikano.RESTobjects;

public class Book {

    private int id;
    private String name;
    private String file;
    private String cover;
    private int grade;
    private String deleted_at;
    private String created_at;
    private String updated_at;

    public Book() {
    }

    public Book(int id, String book, String file, String cover, int grade, String deleted_at, String created_at, String updated_at) {
        this.id = id;
        this.name = book;
        this.file = file;
        this.cover = cover;
        this.grade = grade;
        this.deleted_at = deleted_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book1 = (Book) o;
        return id == book1.id &&
                grade == book1.grade &&
                name.equals(book1.name) &&
                file.equals(book1.file) &&
                cover.equals(book1.cover);
    }

}
