package org.dictionary.repository;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.dictionary.domain.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

@Named
public class DefaultTranslationRepositoryCustom implements TranslationRepositoryCustom {

    private final EntityManager em;

    @Autowired
    public DefaultTranslationRepositoryCustom(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(Translation.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Translation> findTranslationsFromWord(Long wordId, Long toLanguageId) {
        Query q = em
                .createQuery("from Translation t where t.from_word.id = :wordId and t.to_word.language.id = :toLanguageId");
        q.setParameter("wordId", wordId);
        q.setParameter("toLanguageId", toLanguageId);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Translation> findTranslationsToWord(Long wordId, Long toLanguageId) {
        Query q = em
                .createQuery("from Translation t where t.to_word.id = :wordId and t.from_word.language.id = :toLanguageId");
        q.setParameter("wordId", wordId);
        q.setParameter("toLanguageId", toLanguageId);
        return q.getResultList();
    }
}
