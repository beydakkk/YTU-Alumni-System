package com.example.firstapp;

public class HelperClass {

    String name;
    String surname;
    String email;
    String entryDate;
    String graduateDate;

    public String getphotoUri() {
        return photoUri;
    }

    public void setphotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    String photoUri;
    String degree, phone, social, country, city, company;

    public HelperClass() {
    }

    public HelperClass(String name, String surname, String email, String entryDate, String graduateDate) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.entryDate = entryDate;
        this.graduateDate = graduateDate;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getGraduateDate() {
        return graduateDate;
    }

    public void setGraduateDate(String graduateDate) {
        this.graduateDate = graduateDate;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
