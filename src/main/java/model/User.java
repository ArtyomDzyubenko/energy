package model;

public class User {
    private Long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Long phone;
    private String email;
    private Integer personalAccount;
    private boolean admin;
    private String secretKey;

    public User() { }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPersonalAccount(Integer personalAccount) {
        this.personalAccount = personalAccount;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Long getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPersonalAccount() {
        return personalAccount;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
