package com.rom4ik.model;

import javax.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author rom4ik
 */

@Data
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotEmpty(message = "Name can not be empty")
    @Size(min = 2, max = 25, message = "Size must be between 2 and 25 characters")
    private String name;

    @Column(name = "surname")
    @NotEmpty(message = "Surname can not be empty")
    @Size(min = 2, max = 25, message = "Size must be between 2 and 25 characters")
    private String surname;

    @Column(name = "birthday_year")
    @Min(value = 1900, message = "Minimum year should be 1900")
    @Max(value = 2023, message = "Maximum year should be 2023")
    private int birthdayYear;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Book> books;

    @Builder
    public Person(String name, String surname, int birthdayYear) {
        this.name = name;
        this.surname = surname;
        this.birthdayYear = birthdayYear;
    }
}
