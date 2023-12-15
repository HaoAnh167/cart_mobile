package hanu.a2_2001040067.model;

public class Product {
//    private int id;
    private String thumbnail;
    private String name;
    private String category;
    private int unitPrice;

    public Product() {
    }

    public Product(String thumbnail, String name, String category, int unitPrice) {
//        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

//    public int getId() {
//        return id;
//    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
//                "id=" + id +
                "thumbnail='" + thumbnail + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
