package com.example.mathquest;

public class User {
    private String id;
    private String email;
    private String nom;
    private String password;
    public User(String email, String nom, String password,String id) {
        this.email = email;
        this.nom = nom;
        this.password = password;
        this.id = id;
    }
    public User() {
    }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getnom() { return nom; }
    public String getId() { return id; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setNom(String nom) { this.nom = nom; }
    public void setId(String id) { this.id = id; }




}
