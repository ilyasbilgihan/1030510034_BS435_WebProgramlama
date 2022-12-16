package org.webp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max=128)
    private String name;

    @OneToOne(mappedBy = "borrower", cascade = CascadeType.ALL)
    private Book borrowed;

    public Member() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Book book) {
        this.borrowed = book;
    }

}
