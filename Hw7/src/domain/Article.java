package domain;

import java.sql.Date;
import java.util.LinkedList;

public class Article {
    private int id;
    private String title;
    private String brief;
    private String content;
    private Date createDate;
    private boolean isPublished;
    private boolean isFree;
    private Date lastUpdate;
    private Date publishDate;
    private Category category;
    private LinkedList<Tag> list;


    public Article(int id, String title, String brief, String content) {
        this.id = id;
        this.title = title;
        this.brief = brief;
        this.content = content;
        list = new LinkedList<>();

    }

    public Article(String title, String brief, String content) {
        this.title = title;
        this.brief = brief;
        this.content = content;

    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void addTag(Tag tag) {
        list.add(tag);
    }

    public int getId() {
        return id;
    }


    public String getTitle() {
        return title;
    }

    public String getBrief() {
        return brief;
    }

    public String getContent() {
        return content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Category getCategory() {
        return category;
    }

    public void print() {
        System.out.println("Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", isPublished=" + isPublished +
                ", isFree="+isFree+
                ", lastUpdate=" + lastUpdate +
                ", publishDate=" + publishDate +
                ", categoryId=" + category.getId() +
                ", categoryTitle=" + category.getTitle() + "}");
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                ", isPublished=" + isPublished +
                ", isFree="+isFree+
                ", lastUpdate=" + lastUpdate +
                ", publishDate=" + publishDate +
                ", categoryId=" + category.getId() +
                ", categoryTitle=" + category.getTitle() +
                "\n" + "all tag article is : \n " + list +
                '}';
    }
}
