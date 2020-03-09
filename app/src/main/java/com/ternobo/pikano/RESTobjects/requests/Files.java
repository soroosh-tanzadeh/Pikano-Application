package com.ternobo.pikano.RESTobjects.requests;

public class Files {
    private int category;
    private int book;
    private String token;

    public Files(int category, int book, String token) {
        this.category = category;
        this.book = book;
        this.token = token;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }
}
