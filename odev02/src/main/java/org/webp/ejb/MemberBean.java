package org.webp.ejb;

import org.webp.entity.*;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class MemberBean {

    @PersistenceContext
    private EntityManager em;
	
    public MemberBean(){}

    
    // Member Service
    public Long createMember(String name){

        Member member = new Member();
        member.setName(name);

        em.persist(member);

        return member.getId();
    }

    public List<Member> getAllMembers(){
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> members = query.getResultList();
        if(members.size() == 0){
            throw new IllegalArgumentException("There is no member in database");
        }

        return members;
    }

    public Member getMember(long id){
        Member member = em.find(Member.class, id);
        if(member == null){
            throw new IllegalArgumentException("Member with id "+id+" does not exist");
        }

        return member;
    }

}
