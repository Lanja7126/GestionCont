package com.example.gestioncont;

public class Contact {
    private String nom;
    private String telephone;
    private String mail;

    public Contact(String nom, String telephone, String mail) {
        this.nom = nom;
        this.telephone = telephone;
        this.mail = mail;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }
    public String getMail(){return mail;}
}
