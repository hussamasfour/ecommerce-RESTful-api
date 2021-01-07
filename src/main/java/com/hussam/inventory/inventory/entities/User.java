package com.hussam.inventory.inventory.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "First name must be not empty")
    private  String firstname;

    @NotEmpty(message = "Last name must be not empty")
    private String lastName;

    private String mobile;

    @NotEmpty(message = "Username must be not empty")
    @Column(name = "username", unique = true)
    @Pattern(regexp = "^[A-Za-z]\\w{5,29}$", message = "username is not valid")
    private String username;

    @JsonIgnore
    @NotEmpty(message = "Password can't be empty")
    private String password;

    @NaturalId
    @NotEmpty(message = "Email must be not empty")
    @Email(message = "Email should be a valid email")
    @Column(name = "email", unique = true)
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    public User() {
    }

    public User(String username, String password, String email, Date createdAt, String firstname, String lastName, String mobile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdTime = createdAt;
        this.firstname = firstname;
        this.lastName = lastName;
        this.mobile = mobile;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
