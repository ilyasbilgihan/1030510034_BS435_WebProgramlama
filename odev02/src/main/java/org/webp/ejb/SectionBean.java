package org.webp.ejb;

import org.webp.entity.*;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class SectionBean {

    @PersistenceContext
    private EntityManager em;
	
    public SectionBean(){}

    // Section Service
    public Long createSection(String name){

        Section section = new Section();
        section.setName(name);

        em.persist(section);

        return section.getId();
    }

    public List<Section> getAllSections(boolean withBooks){

        TypedQuery<Section> query = em.createQuery("select s from Section s", Section.class);
        List<Section> sections = query.getResultList();
        if(sections.size() == 0){
            throw new IllegalArgumentException("There is no section in database");
        }

        if(withBooks){
            //force loading
            sections.forEach(s -> s.getBooks().size());
        }

        return sections;
    }

    public Section getSection(long id, boolean withBooks){

        Section section = em.find(Section.class, id);
        if(section == null){
            throw new IllegalArgumentException("Section with id "+id+" does not exist");
        }

        if(withBooks){
            section.getBooks().size();
        }

        return section;
    }

}
