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
                .createQuery("from Translation t where t.fromWord.id = :wordId and t.toWord.language.id = :toLanguageId");
        q.setParameter("wordId", wordId);
        q.setParameter("toLanguageId", toLanguageId);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Translation> findTranslationsToWord(Long wordId, Long toLanguageId) {
        Query q = em
                .createQuery("from Translation t where t.toWord.id = :wordId and t.fromWord.language.id = :toLanguageId");
        q.setParameter("wordId", wordId);
        q.setParameter("toLanguageId", toLanguageId);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Translation findTranslation(Long wordId1, Long wordId2) {
        // ideally we need 2 constraints to that (fromWord, toWord) is unique
        Query q = em.createQuery("from Translation t where t.fromWord.id = :wordId1 and t.toWord.id = :wordId2");
        q.setParameter("wordId1", wordId1);
        q.setParameter("wordId2", wordId2);
        List<Translation> t = q.getResultList();
        if (t.isEmpty()) {
            return null;
        }
        return (Translation) t.get(0);
    }
}
