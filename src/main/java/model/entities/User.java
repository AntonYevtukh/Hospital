package model.entities;

import enums.Gender;
import validation.UserValidator;
import validation.ValidatedEntity;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;

@ValidatedEntity(validatorClass = UserValidator.class)
public class User implements Entity {
    private long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private Date dateOfBirth;
    private Gender gender;
    private String passportNumber;
    private String email;
    private String phone;
    private String address;
    private String language;
    private int itemsPerPage;
    private Photo photo;
    private Map<Long, Role> roleMap;
    private boolean hospitalized;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public long getId() {
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

    public String getMiddleName() {
        return middleName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getLanguage() {
        return language;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Map<Long, Role> getRoleMap() {
        return roleMap;
    }

    public boolean isHospitalized() {
        return hospitalized;
    }

    public void setId(long id) {
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

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public void setPhoto(Photo phote) {
        this.photo = phote;
    }

    public void setRoleMap(Map<Long, Role> roleMap) {
        this.roleMap = roleMap;
    }

    public void setHospitalized(boolean hospitalized) {
        this.hospitalized = hospitalized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", passportNumber='" + passportNumber + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", language='" + language + '\'' +
                ", itemsPerPage=" + itemsPerPage +
                '}';
    }
}
