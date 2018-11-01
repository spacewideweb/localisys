package atpl.cc.localisys.classes;


import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.Map;

import java.util.ArrayList;

/**
 * Created by designer on 3/3/18.
 */

public class PostClass {


    public atpl.cc.localisys.activities.classes.Location location;

    public String description;

    public ArrayList<String> hashtags = null;

    public ArrayList<String> imageURLs = null;

    public Boolean isPrivate;

    public Boolean isSecret;

    public String postCreatorID;

    public String status;

    public String timestamp;

    public String title;

    public String type;

    public String validTime;

    //Today
    public Price price;
    //Today
    public Timeline timeline;

    public Map<String,Object> taggedUsers;


    public PostClass() {

    }

    public PostClass(atpl.cc.localisys.activities.classes.Location location, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isPrivate, Boolean isSecret, String postCreatorID, String timestamp, String title, String type, Price price, Timeline timeline) {
        this.location = location;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isPrivate = isPrivate;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        /*this.status = status;*/
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.price = price;
        this.timeline = timeline;
    }

    public PostClass(atpl.cc.localisys.activities.classes.Location location, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isPrivate, Boolean isSecret, String postCreatorID, String timestamp, String title, String type, String validTime, Price price, Timeline timeline, Map<String,Object> taggedUsers) {
        this.location = location;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isPrivate = isPrivate;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        /*this.status = status;*/
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.validTime = validTime;
        this.price=price;
        this.timeline=timeline;
        this.taggedUsers = taggedUsers;
    }

    public PostClass(atpl.cc.localisys.activities.classes.Location location, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isPrivate, Boolean isSecret, String postCreatorID, String timestamp, String title, String type, Price price, Timeline timeline, Map<String,Object> taggedUsers) {
        this.location = location;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isPrivate = isPrivate;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        /*this.status = status;*/
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.price = price;
        this.timeline = timeline;
        this.taggedUsers = taggedUsers;
    }

    public PostClass(atpl.cc.localisys.activities.classes.Location location, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isPrivate, Boolean isSecret, String postCreatorID, String timestamp, String title, String type, String validTime, Map<String,Object> taggedUsers) {
        this.location = location;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isPrivate = isPrivate;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        /*this.status = status;*/
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.validTime = validTime;
        this.taggedUsers = taggedUsers;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public atpl.cc.localisys.activities.classes.Location getLocation() {
        return location;
    }

    public void setLocation(atpl.cc.localisys.activities.classes.Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(ArrayList<String> hashtags) {
        this.hashtags = hashtags;
    }

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(ArrayList<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsSecret() {
        return isSecret;
    }

    public void setIsSecret(Boolean isSecret) {
        this.isSecret = isSecret;
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

    public Map<String, Object> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(Map<String, Object> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    /*second classss*/





}
