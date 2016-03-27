package org.dictionary.repository;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.dictionary.domain.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

@Named
public class DefaultTagRepositoryCustom implements TagRepositoryCustom {

    private final EntityManager em;

    @Autowired
    public DefaultTagRepositoryCustom(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(Tag.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Tag findByName(String name) {
        Query q = em.createQuery("from Tag where name = :name");
        q.setParameter("name", name);
        // TODO make tag name unique
        List<Tag> tags = q.getResultList();
        if (tags.isEmpty()) {
            return null;
        }
        return tags.get(0);
    }
}
