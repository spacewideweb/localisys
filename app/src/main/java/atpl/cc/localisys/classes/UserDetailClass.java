package atpl.cc.localisys.classes;

/**
 * Created by android on 22/11/17.
 */

public class UserDetailClass {

    String address="";
    String country="";
    String countryCode="";
    String dob="";
    String email="";
    String firstName="";
    boolean isOnline;
    String lastName="";
    String phoneNumber="";
    String postCode="";
    String profileImageURL="";
    String state="";
    String username="";

    public UserDetailClass() {
    }

    public UserDetailClass(String address, String country, String countryCode, String dob, String email, String firstName, boolean isOnline, String lastName, String phoneNumber, String postCode, String profileImageURL, String state, String username) {
        this.address = address;
        this.country = country;
        this.countryCode = countryCode;
        this.dob = dob;
        this.email = email;
        this.firstName = firstName;
        this.isOnline = isOnline;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.postCode = postCode;
        this.profileImageURL = profileImageURL;
        this.state = state;
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
