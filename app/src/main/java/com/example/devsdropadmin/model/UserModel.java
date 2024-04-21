package com.example.devsdropadmin.model;


public class UserModel {
    String email;
    String username;
    String userId;
    String profession;
    String profile;
    int followersCount;
    int followingCount;
    int numberOfPosts;
    Boolean isDeleted;



    public UserModel(String email, String username, String userId, String profession, String profile, int followersCount, int followingCount, Boolean isDeleted,int numberOfPosts) {
        this.email = email;
        this.username = username;
        this.userId = userId;
        this.profession = profession;
        this.profile = profile;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.isDeleted = isDeleted;
        this.numberOfPosts = numberOfPosts;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public UserModel() {
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
