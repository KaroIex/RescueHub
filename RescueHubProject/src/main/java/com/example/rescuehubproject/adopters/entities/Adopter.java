package com.example.rescuehubproject.adopters.entities;

import com.example.rescuehubproject.accounts.entity.User;
import com.example.rescuehubproject.adoption.entity.Adoption;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ADOPTERS")
public class Adopter {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_adopter", nullable = false)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "adopter")
    private Set<Adoption> adoptions;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id_person")
    private User user;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip")
    private String zip;

    @Column(name = "country")
    private String country;
}