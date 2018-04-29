package ca.saileshbechar.imhere.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Sailesh on 3/11/2018.
 */
public class PlaceInfo {
    private String name;
    private String address;
    private String id;
    private LatLng latlng;
    private String attributions;

    public PlaceInfo(String name, String address, String id,
                     LatLng latlng, String attributions) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latlng = latlng;
        this.attributions = attributions;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }


    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(String attributions) {
        this.attributions = attributions;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latlng=" + latlng +
                ", attributions='" + attributions + '\'' +
                '}';
    }

}
