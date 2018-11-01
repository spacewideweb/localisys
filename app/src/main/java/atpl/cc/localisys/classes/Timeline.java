package atpl.cc.localisys.classes;

public class Timeline {

    String date;
    String type;
    String startDate;
    String endDate;

    public Timeline() {
    }

    public Timeline(String type) {
        this.type = type;
    }

    public Timeline(String type, String startDate, String endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Timeline(String type,String date) {
        this.type = type;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
