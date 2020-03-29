package pl.moja.bibloteka.database.models;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "BOOKS")
public class Book  implements BaseModel {

    public static final String AUTHOR_ID = "AUTHOR_ID";
    public static final String CATEGORY_ID = "CATEGORY_ID";

    public Book() {

    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = AUTHOR_ID, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
    private Author author;

    @DatabaseField(columnName = CATEGORY_ID, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, canBeNull = false)
    private Category category;

    @DatabaseField(columnName = "TITLE", canBeNull = false)
    private String title;

    @DatabaseField(columnName = "DESCRIPTION")
    private String description;

    @DatabaseField(columnName = "RELEASE_DATE")
    private Date dateRelease;

    @DatabaseField(columnName = "ISBN")
    private String isbn;

    @DatabaseField(columnName = "RATING", width = 1)
    private int rating;

    @DatabaseField(columnName = "ADDED_DATE")
    private Date addedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getDateRelease() { return dateRelease; }

    public void setDateRelease(Date dateRelease) { this.dateRelease = dateRelease; }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

}