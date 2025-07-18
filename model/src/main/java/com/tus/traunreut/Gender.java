package com.tus.traunreut;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "genders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gender {
    @Id
    @Column(name = "gender_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Gender gender = (Gender) o;
        return Objects.equals(name, gender.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
