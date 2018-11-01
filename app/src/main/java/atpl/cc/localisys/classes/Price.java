package atpl.cc.localisys.classes;

public class Price {

    public Integer maximumAmount;
    public Integer minimumAmount;
    public String  type;
    public Integer amount;

    public Price() {
    }

    public Price(String type) {
        this.type = type;
    }

    public Price(Integer amount,String type) {
        this.amount = amount;
        this.type = type;
    }

    public Price(Integer maximumAmount, Integer minimumAmount, String type) {
        this.maximumAmount = maximumAmount;
        this.minimumAmount = minimumAmount;
        this.type = type;
    }

    public Integer getMaximumAmount() {
        return maximumAmount;
    }

    public void setMaximumAmount(Integer maximumAmount) {
        this.maximumAmount = maximumAmount;
    }

    public Integer getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(Integer minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
