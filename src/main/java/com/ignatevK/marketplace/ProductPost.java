package com.ignatevK.marketplace;

public class ProductPost {

    private String price;
    private String title; // название

    private String detailsLink;

    private String imagineLink;

    public String getImagineLink() {
        return imagineLink;
    }

    public void setImagineLink(String imagineLink) {
        this.imagineLink = imagineLink;
    }

    public String getDetailsLink() {
        return detailsLink;
    }

    public void setDetailsLink(String detailsLink) {
        this.detailsLink = detailsLink;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProductPost{" +
                "price='" + price + '\'' +
                ", title='" + title + '\'' +
                ", detailsLink='" + detailsLink + '\'' +
                ", imagineLink='" + imagineLink + '\'' +
                '}';
    }

}
