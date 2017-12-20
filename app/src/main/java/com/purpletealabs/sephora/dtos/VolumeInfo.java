
package com.purpletealabs.sephora.dtos;

import java.util.List;

public class VolumeInfo {

    private String title;

    private String subtitle;

    private List<String> authors;

    private String publisher;

    private String publishedDate;

    private String description;

    private Integer pageCount;

    private List<String> categories = null;

    private float averageRating;

    private int ratingsCount;

    private String contentVersion;

    private ImageLinks imageLinks;

    private String language;

    private String previewLink;

    private String infoLink;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAuthors() {
        StringBuilder sbAuthors = new StringBuilder();
        if (authors == null) {
            sbAuthors.append("Author: NA");
        } else if (authors.size() > 1) {
            sbAuthors.append("Authors: ");
            for (int i = 0; i < authors.size(); i++) {
                String author = authors.get(i);
                sbAuthors.append(author);
                if (i < authors.size() - 1) {
                    sbAuthors.append(", ");
                }
            }
        } else {
            sbAuthors.append("Author: ");
            sbAuthors.append(authors.get(0));
        }
        return sbAuthors.toString();
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return String.format("Published By: %s", publisher == null ? "NA" : publisher);
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }

    public String getContentVersion() {
        return contentVersion;
    }

    public void setContentVersion(String contentVersion) {
        this.contentVersion = contentVersion;
    }

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }
}
