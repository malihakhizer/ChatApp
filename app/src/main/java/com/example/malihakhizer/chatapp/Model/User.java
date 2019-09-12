package com.example.malihakhizer.chatapp.Model;

public class User {
    public String id;
    public String name;
    public String email;
    public String password;
    public String url;


    public User() {

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }


    public User(String email, String password, String name, String url, String id) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.url = url;
        this.id = id;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
