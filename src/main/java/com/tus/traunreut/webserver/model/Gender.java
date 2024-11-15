package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "genders")
public class Gender {

    @Id
    @Column(name = "gender_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Gender(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Gender() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String gender) {
        this.name = gender;
    }
}
