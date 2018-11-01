package atpl.cc.localisys.classes;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by designer on 3/3/18.
 */

public class SkillPost {

    public atpl.cc.localisys.activities.classes.Location location;

    //Today
    public Price price;

    public String description;

    public ArrayList<String> hashtags = null;

    public ArrayList<String> imageURLs = null;

    public Boolean isSecret;

    public String postCreatorID;

    public String suspension;

    public String suspensionDate;

    public String timestamp;

    public String title;

    public String type;

    public Map<String,Object> taggedUsers;

    public String validTime;

    public SkillPost() {
    }

    public SkillPost(atpl.cc.localisys.activities.classes.Location location, Price price, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isSecret, String postCreatorID, String suspension, String timestamp, String title, String type) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        this.suspension = suspension;
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
    }

    public SkillPost(atpl.cc.localisys.activities.classes.Location location, Price price, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isSecret, String postCreatorID, String suspension, String timestamp, String title, String type, Map<String,Object> taggedUsers) {
        this.location = location;
        this.price = price;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        this.suspension = suspension;
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.taggedUsers = taggedUsers;
    }

    public SkillPost(atpl.cc.localisys.activities.classes.Location location, String description, ArrayList<String> hashtags, ArrayList<String> imageURLs, Boolean isSecret, String postCreatorID, String suspension, String timestamp, String title, String type, Map<String,Object> taggedUsers, String validTime) {
        this.location = location;
        this.description = description;
        this.hashtags = hashtags;
        this.imageURLs = imageURLs;
        this.isSecret = isSecret;
        this.postCreatorID = postCreatorID;
        this.suspension = suspension;
        this.timestamp = timestamp;
        this.title = title;
        this.type = type;
        this.taggedUsers = taggedUsers;
        this.validTime = validTime;
    }

    public atpl.cc.localisys.activities.classes.Location getLocation() {
        return location;
    }

    public void setLocation(atpl.cc.localisys.activities.classes.Location location) {
        this.location = location;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
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

    public Boolean getSecret() {
        return isSecret;
    }

    public void setSecret(Boolean secret) {
        isSecret = secret;
    }

    public String getPostCreatorID() {
        return postCreatorID;
    }

    public void setPostCreatorID(String postCreatorID) {
        this.postCreatorID = postCreatorID;
    }

    public String getSuspension() {
        return suspension;
    }

    public void setSuspension(String suspension) {
        this.suspension = suspension;
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

    public String getSuspensionDate() {
        return suspensionDate;
    }

    public void setSuspensionDate(String suspensionDate) {
        this.suspensionDate = suspensionDate;
    }

    public Map<String, Object> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(Map<String, Object> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }
}
