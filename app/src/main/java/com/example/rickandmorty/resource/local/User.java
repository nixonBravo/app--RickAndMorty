package com.example.rickandmorty.resource.local;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("username")
    private String username;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("access_token")
    private String accessToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    // Constructor con número variable de argumentos
    public User(String... args) {
        // Asignar los valores de los argumentos a los campos, si existen
        if (args.length > 0) {
            this.username = args[0];
        }
        if (args.length > 1) {
            this.email = args[1];
        }
        if (args.length > 2) {
            this.password = args[2];
        }
        if (args.length > 3) {
            this.accessToken = args[3];


        }

    }

}