package atpl.cc.localisys.modal;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

/**
 * Created by user5 on 9/6/17.
 */

@IgnoreExtraProperties
public class User {

    public String address;

    public String country;

    public String countryCode;

    public String dob;

    public String email;

    public String firstName;

    public Boolean isOnline;

    public String lastName;

    public String phoneNumber;

    public String postCode;

    public String profileImageURL;

    public String state;

    public String username;

    public String biography;


    public User() {
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public User(String address, String country, String countryCode, String dob, String email, String firstName, Boolean isOnline, String lastName, String phoneNumber, String postCode, String profileImageURL, String state, String username, String biography, ArrayList<String> interestHashtags, ArrayList<String> skillHashtags) {
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
        this.biography = biography;
    }

    public User(String address, String country, String countryCode, String dob, String email, String firstName, Boolean isOnline, String lastName, String phoneNumber, String postCode, String profileImageURL, String state, String username , String biography) {
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
        this.biography = biography;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
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

  
    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
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
