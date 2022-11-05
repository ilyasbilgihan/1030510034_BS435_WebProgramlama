package org.webp.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    public Book() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Section getSection() { return section; }

    public void setSection(Section section) { this.section = section; }

    public Author getAuthor() { return author; }

    public void setAuthor(Author author) { this.author = author; }
}
