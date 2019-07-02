package com.company.energy.model;

public class MeterReader {
    private Long id;
    private Integer number;
    private String IPAddress;
    private Integer port;

    public MeterReader() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public Integer getPort() {
        return port;
    }

    public Integer getNumber() {
        return number;
    }
}

