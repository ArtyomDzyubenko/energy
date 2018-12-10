package model;

import java.time.LocalDate;

public class Invoice {
    private Long id;
    private LocalDate date;
    private Double consumption;
    private Double price;
    private Long userId;
    private boolean paid;
    private String secretKey;
    private MeterEntity meter = new MeterEntity();
    private Measurement startValue = new Measurement();
    private Measurement endValue = new Measurement();

    public Invoice(){}

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMeter(MeterEntity meter) {
        this.meter = meter;
    }

    public void setStartValue(Measurement startValue) {
        this.startValue = startValue;
    }

    public void setEndValue(Measurement endValue) {
        this.endValue = endValue;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public MeterEntity getMeter() {
        return meter;
    }

    public Measurement getStartValue() {
        return startValue;
    }

    public Measurement getEndValue() {
        return endValue;
    }

    public Double getConsumption() {
        return consumption;
    }

    public Double getPrice() {
        return price;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isPaid() {
        return paid;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
