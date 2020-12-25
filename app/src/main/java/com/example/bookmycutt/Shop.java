package com.example.bookmycutt;
//
//public class Shop {
//    private String Type;
//
//    public String getImage() {
//        return Image;
//    }
//
//    public void setImage(String image) {
//        Image = image;
//    }
//
//    private String Image;
//
//    public Shop(String type, String shopname, String locality,String Image) {
//        this.Type = type;
//        this.Shopname = shopname;
//        this.Locality = locality;
//        this.Image=Image;
//    }
////
////    public Shop(String type,String shopname,String locality)
////    {}
//
//    public Shop() {}
//
//    private String Shopname;
//
//    public String getType() {
//        return Type;
//    }
//
//    public void setType(String type) {
//        Type = type;
//    }
//
//    public String getShopname() {
//        return Shopname;
//    }
//
//    public void setShopname(String shopname) {
//        Shopname = shopname;
//    }
//
//    public String getLocality() {
//        return Locality;
//    }
//
//    public void setLocality(String locality) {
//        Locality = locality;
//    }
//
//    private String Locality;
//}


public class Shop{
    private String Image;
    private String Type;

    public Shop() {
    }

    public Shop(String image, String type, String shopname, String locality) {
        Image = image;
        Type = type;
        Shopname = shopname;
        Locality = locality;
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getShopname() {
        return Shopname;
    }

    public void setShopname(String shopname) {
        Shopname = shopname;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    private String Shopname;
    private String Locality;

}
