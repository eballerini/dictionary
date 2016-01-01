package org.dictionary.repository;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.dictionary.domain.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

@Named
public class DefaultLanguageRepositoryCustom implements LanguageRepositoryCustom {

    private final EntityManager em;

    @Autowired
    public DefaultLanguageRepositoryCustom(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(Language.class);
    }

    @Override
    public Language findByLanguage(String language) {
        Query q = em.createQuery("from Language where language = :language");
        q.setParameter("language", language);
        Object languageAsObj = q.getSingleResult();
        if (languageAsObj == null) {
            return null;
        }
        return (Language) languageAsObj;
    }

}
