package com.example.newsapp.Model;

public class NewsListModel {

    String sourceId,sourceName,title,description,content,authorName,clickURL,thumbnailURL;

    public NewsListModel(String sourceId, String sourceName, String title, String description, String content, String authorName, String clickURL, String thumbnailURL) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.title = title;
        this.description = description;
        this.content = content;
        this.authorName = authorName;
        this.clickURL = clickURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getClickURL() {
        return clickURL;
    }

    public void setClickURL(String clickURL) {
        this.clickURL = clickURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
