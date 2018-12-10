package model;

import java.sql.Timestamp;

public class Measurement {
    private Long id;
    private Timestamp dateTime;
    private String localDateTimeString;
    private Double value;
    private Long meterId;
    private String secretKey;

    public Measurement(){}

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getId() {
        return id;
    }

    public void setDateTime(Timestamp dateTime) {
        setLocalDateTimeString(dateTime.toString().replace(" ", "T"));
        this.dateTime = dateTime;
    }

    public void setLocalDateTimeString(String localDateTimeString) {
        this.localDateTimeString = localDateTimeString;
    }

    public Double getValue() {
        return value;
    }

    public Long getMeterId() {
        return meterId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getLocalDateTimeString() {
        return localDateTimeString;
    }
}
