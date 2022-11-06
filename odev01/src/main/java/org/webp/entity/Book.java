package org.webp.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Future;
import java.util.Date;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max=256)
    private String name;

    @ManyToOne
    @NotNull
    private Section section;

    @ManyToOne
    @NotNull
    private Author author;

    @OneToOne
    private Member borrower;

    @Future
    private Date borrowedUntil;

    public Book() { }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Section getSection() { return section; }

    public void setSection(Section section) { this.section = section; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }

    public Member getBorrower() { return borrower; }

    public void setBorrower(Member member) { this.borrower = member; }

    public Date getBorrowedUntil() { return borrowedUntil; }

    public void setBorrowedUntil(Date date){ this.borrowedUntil = date; }
}
