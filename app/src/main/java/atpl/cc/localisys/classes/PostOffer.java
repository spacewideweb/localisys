package atpl.cc.localisys.classes;

/**
 * Created by designer on 3/3/18.
 */

public class PostOffer {

    Price price;
    Timeline timeline;
    String postId;
    String timestamp;
    String userId;

    public PostOffer() {
    }

    public PostOffer(Price price, Timeline timeline, String postId, String timestamp, String userId) {
        this.price = price;
        this.timeline = timeline;
        this.postId = postId;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
