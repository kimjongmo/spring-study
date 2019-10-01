package com.spring.jpa.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Employee {

    @Id
    @Column(name = "EMPLOYEE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "NAME")
    private String name;

    @Column(name = "EMPLOYEE_NUM")
    private String employeeNumber;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="address1",column = @Column(name="HOME_ADDR1")),
            @AttributeOverride(name="address2",column = @Column(name="HOME_ADDR2")),
            @AttributeOverride(name="zipcode",column = @Column(name="HOME_ZIPCODE"))
    })
    private Address address;

    @Column(name = "BIRTH_YEAR")
    private Integer birthYear;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Temporal(TemporalType.DATE)
    @Column(name="JOINED_DATE")
    private Date joinedDate;

    public Employee(String empNum,String name, Address address, int birthYear,Team team, Date joinedDate){
        this.employeeNumber = empNum;
        this.name = name;
        this.address = address;
        this.birthYear = birthYear;
        this.team = team;
        this.joinedDate = joinedDate;
    }

    protected Employee(){
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public Address getAddress() {
        return address;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public Team getTeam() {
        return team;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setAddress(Address address){
        this.address = address;
    }

    public void changeTeam(Team newTeam){

    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", employeeNumber='" + employeeNumber + '\'' +
                ", address=" + address +
                ", birthYear=" + birthYear +
                ", team=" + team +
                ", joinedDate=" + joinedDate +
                '}';
    }
}
