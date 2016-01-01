package org.dictionary.repository;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.dictionary.domain.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;


@Named
public class DefaultWordRepositoryCustom implements WordRepositoryCustom {

    private final EntityManager em;

    @Autowired
    public DefaultWordRepositoryCustom(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(Word.class);
    }

    @Override
    public int countWordsInLanguage(long id) {
        Query q = em.createQuery("select count(*) from Word w where w.language.id = :languageId");
        q.setParameter("languageId", id);
        int num = ((Long) q.getSingleResult()).intValue();
        return num;
    }

    @Override
    public Word loadWord(long id, int offset) {
        Query q = em.createQuery("from Word w where w.language.id = :languageId order by w.id");
        q.setParameter("languageId", id);
        q.setMaxResults(1);
        q.setFirstResult(offset);
        Object o = q.getSingleResult();
        if (o == null) {
            return null;
        }
        return (Word) o;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Word loadWord(String word, Long languageId) {
        Query q = em.createQuery("from Word w where w.word = :word and w.language.id = :languageId");
        q.setParameter("word", word);
        q.setParameter("languageId", languageId);
        List<Object> o = q.getResultList();
        if (o.isEmpty()) {
            return null;
        }
        return (Word) o.get(0);
    }

}
