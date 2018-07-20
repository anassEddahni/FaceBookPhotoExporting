package app.adria.com.fbphotosexporting.entities;

public class Photo {

    private String id;
    private String url;
    private String name;

    public Photo() {
    }

    public Photo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Photo(String url) {
        this.url = url;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        // Set a default image in case of null or empty
        // Or we can use the place holder support of picasso library
        return url == null || url.isEmpty() ? "https://www.technodoze.com/wp-content/uploads/2016/03/default-placeholder-750x415.png" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
