package model;

public class Meter {
    private Long id;
    private Integer number;
    private Long meterReaderId;
    private Integer meterReaderNumber;
    private Resource resource = new Resource();
    private Measurement measurement = new Measurement();
    private Long addressId;
    private String secretKey;

    public Meter(){}

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setMeterReaderId(Long meterReaderId) {
        this.meterReaderId = meterReaderId;
    }

    public void setMeterReaderNumber(Integer meterReaderNumber) {
        this.meterReaderNumber = meterReaderNumber;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumber() {
        return number;
    }

    public Resource getResource() {
        return resource;
    }

    public Long getMeterReaderId() {
        return meterReaderId;
    }

    public Integer getMeterReaderNumber() {
        return meterReaderNumber;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public Long getAddressId() {
        return addressId;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
