package atpl.cc.localisys.modal;

import java.util.ArrayList;

import atpl.cc.localisys.activities.classes.Location;

/**
 * Created by user5 on 17/6/17.
 */

public class AddService {

    public String description="";
    public ArrayList<String> hashtags;
    public ArrayList<String> imageURLs;
    public boolean isPrivate=false;
    public boolean isSecret=false;
    public String postCreatorID="";
    public String status="";
    public String timestamp="";
    public String title="";
    public String type="";
    public String validTime="";
    public String priceType="";
    public String price="";
    public Location location;
    public String timeline="";


    public AddService() {}

    public AddService(String description,ArrayList<String> hashtags, ArrayList<String> imageURLs, boolean isPrivate, boolean isSecret, String postCreatorID, String status, String timestamp, String title, String type, String validTime,String priceType,String price,Location location,String timeline) {
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isPrivate = isPrivate;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        this.status = status;
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.validTime = validTime;
        this.price=price;
        this.priceType=priceType;
        this.location=location;
        this.timeline=timeline;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String>  getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<String>  hashtags) {
        this.hashtags = hashtags;
    }

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isSecret() {
        return isSecret;
    }

    public void setSecret(boolean secret) {
        isSecret = secret;
    }

    public String getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(String postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }
}



