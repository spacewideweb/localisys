package atpl.cc.localisys.modal;

/**
 * Created by android on 1/3/18.
 */

public class FollowList{

    public FollowList() {
    }

    String key;
    boolean value;



    public FollowList(String key, boolean value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean  getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}