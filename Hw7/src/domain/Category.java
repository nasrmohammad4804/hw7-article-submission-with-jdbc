package domain;

public class Category {
    private int id;
    private String title;
    private String description;

//    public Category(){}

    public Category(int id) {
        this.id = id;

    }

    public Category(int id, String title, String description) {

        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Category(int id, String title) {
        this(id, title, null);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

