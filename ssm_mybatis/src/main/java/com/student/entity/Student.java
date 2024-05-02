package com.student.entity;

import java.util.Date;

/**
 * ProjectName:  Persistencelayer
 * PackageName:  com.student
 * ClassName:    Student
 *
 * @Author chnpngwng
 * @Date 2024 05 02 20 24
 **/

public class Student {
    private Integer studentId;

    private String username;

    private String passwordHash;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String gender;

    private String address;

    private String email;

    private String phoneNumber;

    private String guardianName;

    private String guardianPhoneNumber;

    private Date admissionDate;

    private Date graduationDate;

    private String department;

    private String major;

    private Integer currentYear;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianPhoneNumber() {
        return guardianPhoneNumber;
    }

    public void setGuardianPhoneNumber(String guardianPhoneNumber) {
        this.guardianPhoneNumber = guardianPhoneNumber;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getGraduationDate() {
        return graduationDate;
    }

    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Integer getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(Integer currentYear) {
        this.currentYear = currentYear;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", guardianName='" + guardianName + '\'' +
                ", guardianPhoneNumber='" + guardianPhoneNumber + '\'' +
                ", admissionDate=" + admissionDate +
                ", graduationDate=" + graduationDate +
                ", department='" + department + '\'' +
                ", major='" + major + '\'' +
                ", currentYear=" + currentYear +
                '}';
    }

    public Student() {
        super();
    }

    public Student(Integer studentId, String username, String passwordHash, String firstName, String lastName, Date dateOfBirth, String gender, String address, String email, String phoneNumber, String guardianName, String guardianPhoneNumber, Date admissionDate, Date graduationDate, String department, String major, Integer currentYear) {
        this.studentId = studentId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.guardianName = guardianName;
        this.guardianPhoneNumber = guardianPhoneNumber;
        this.admissionDate = admissionDate;
        this.graduationDate = graduationDate;
        this.department = department;
        this.major = major;
        this.currentYear = currentYear;
    }
}