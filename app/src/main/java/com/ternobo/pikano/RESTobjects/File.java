package com.ternobo.pikano.RESTobjects;

public class File {
    private int id;
    private String name;
    private String file;
    private int book;
    private String category;
    private String created_at;
    private String updated_at;
    private boolean is_bookmarked;
    private String book_name;
    private String cover;
    private String category_name;
    private String average_rate;
    private int grade;


    public File() {
    }

    public File(int id, String name, String file, int book, String category, String created_at, String updated_at, boolean is_bookmarked) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.book = book;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.is_bookmarked = is_bookmarked;
    }


    public File(int id, String name, String file, int book, String category, String created_at, String updated_at, boolean is_bookmarked, String book_name, String cover, String category_name, String average_rate, int grade) {
        this.id = id;
        this.name = name;
        this.file = file;
        this.book = book;
        this.category = category;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.is_bookmarked = is_bookmarked;
        this.book_name = book_name;
        this.cover = cover;
        this.category_name = category_name;
        this.average_rate = average_rate;
        this.grade = grade;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getAverage_rate() {
        return average_rate;
    }

    public void setAverage_rate(String average_rate) {
        this.average_rate = average_rate;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
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

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public boolean isBookmarked() {
        return is_bookmarked;
    }

    public void isBookmarked(boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file1 = (File) o;
        return getId() == file1.getId() &&
                getBook() == file1.getBook() &&
                getName().equals(file1.getName()) &&
                getFile().equals(file1.getFile()) &&
                getCategory().equals(file1.getCategory());
    }

}
