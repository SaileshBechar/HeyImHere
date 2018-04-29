package ca.saileshbechar.imhere.models;

import java.io.Serializable;

/**
 * Created by Sailesh on 4/2/2018.
 */

public class PersonObject implements Serializable{
    private String name;
    private String phoneNumber;
    private int _id;
    private double lat;
    private double lng;
    private int radius;


    public PersonObject() {
        this.name = "NotARealName";
        this.phoneNumber = "9059231801";
        this._id = 1;
        this.lat = 0;
        this.lng = 0;
        this.radius = 100;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void set_id(int id){
        this._id = id;
    }
    public int get_id(){
        return _id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String printPerson(){
        String print;
        print = getName() + " " + getPhoneNumber()
                + " " + get_id() + " " +
                getLat() + " " + getLng() + " " + getRadius();

        return print;
    }
}
