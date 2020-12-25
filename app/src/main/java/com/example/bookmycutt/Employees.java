package com.example.bookmycutt;

import android.app.Application;

public class Employees extends Application {
    int j = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public int getService_count() {
        return service_count;
    }

    public void setService_count(int service_count) {
        this.service_count = service_count;
    }

    private int service_count;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position, int cost,String Image) {
        this.position = position;
        String isThere = "null";
        for (int i = 0; i < finalService.length; i++) {
            if (position == finalService[i]) {
                isThere = "True";
            } else {
                isThere = "False";
            }
        }
        if (isThere == "False") {
            finalService[j] = this.position;
            finalCost[j] = String.valueOf(cost);
            Images[j]=Image;
            j++;
        }
    }


    private String position;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    int total;

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    int itemCount;
    String shopname;

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }


    String[]  LocationPrefrence=new  String[100];
    String locPref=null;
    String[] costChecked = new String[100];
String username;
    String shopLocation;
    String[] uid = new String[100];
    String[] shopnames = new String[100];
    String[] locations = new String[100];
    String[] selectedServices = new String[100];
    String[] Cost = new String[100];
    String[] itemCheck = new String[100];
    String[] finalService = new String[100];
    String[] time = new String[100];
    String[] selectedEmployees = new String[100];
    String[] finalCost = new String[100];
    String[] ShopNameForCancel = new String[100];
    String[] ServiceNameForCancel = new String[50];
    String[] TotalForCancel = new String[100];
    String[] OwnerUidForCancel = new String[100];
    String[] Keys = new String[100];

 String userkey;




    String ShopCancel,ServiceCalcel,TotalCalcel,OwnerCalncel,key,uidForBooked;
    int[] mainCost = new int[100];
    String OwnerUid;
    String[] plusminus = new String[100];
    String[] Images = new String[100];
    String[] userUids = new String[100];

    String date, timeofBooking,finalUserUid;
    int totalCost = 0;
    String SelectedEmployee = null;
    int year, day, month;

    int i=0;
    public void resetForBooking() {
        for (int i = 0; i < 100; i++) {
            selectedEmployees[i]=null;
            selectedServices[i]=null;
            plusminus[i]="0";

            itemCheck[i]=null;
            Cost[i]="0";
            time[i]="0";
        }


        totalCost = 0;
        SelectedEmployee=null;
        total=0;
        itemCount=0;
    }
}