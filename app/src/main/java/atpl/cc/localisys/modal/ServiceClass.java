package atpl.cc.localisys.modal;

import java.util.ArrayList;

/**
 * Created by user5 on 27/6/17.
 */

public class ServiceClass {

    public ArrayList<String> image;
    public String title;
    public String description;
    public String hashtag;
    public String location;
    public String price;
    public String timeline;
    public String email;
    public String type;
    public String valid_time;
    public String user_name;
    public String user_image;

    public ServiceClass() {
    }

    public ServiceClass(ArrayList<String> image, String title, String description, String hashtag, String location, String price, String timeline, String email, String type, String valid_time,String user_name,String user_image) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.hashtag = hashtag;
        this.location = location;
        this.price = price;
        this.timeline = timeline;
        this.email = email;
        this.type = type;
        this.valid_time = valid_time;
        this.user_name = user_name;
        this.user_image = user_image;
    }
}




