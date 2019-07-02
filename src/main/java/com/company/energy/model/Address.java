package com.company.energy.model;

public class Address {
    private Long id;
    private Street street = new Street();
    private String building;
    private String flat;
    private Long userId;
    private String secretKey;

    public Address() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getId() {
        return id;
    }

    public Street getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public String getFlat() {
        return flat;
    }

    public Long getUserId() {
        return userId;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
