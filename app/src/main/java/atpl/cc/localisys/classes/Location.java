package atpl.cc.localisys.activities.classes;

/**
 * Created by android on 30/11/17.
 */

public class Location {

    private String addressString;

    private Double latitude;

    private Double longitude;

    private Integer surroundingDistance;


    public Location() {
    }

    public Location(String addressString, Double latitude, Double longitude, Integer surroundingDistance) {
        this.addressString = addressString;
        this.latitude = latitude;
        this.longitude = longitude;
        this.surroundingDistance = surroundingDistance;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getSurroundingDistance() {
        return surroundingDistance;
    }

    public void setSurroundingDistance(Integer surroundingDistance) {
        this.surroundingDistance = surroundingDistance;
    }
}
