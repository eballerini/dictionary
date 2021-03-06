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
    public int countWordsInLanguageWithTag(long languageId, long tagId) {
        Query q = em
                .createQuery("select count(*) from Word w join w.tags t where t.id = :tagId and w.language.id = :languageId ");
        q.setParameter("languageId", languageId);
        q.setParameter("tagId", tagId);
        int num = ((Long) q.getSingleResult()).intValue();
        return num;
    }

    @Override
    public Word loadWord(long languageId, int offset) {
        Query q = em.createQuery("from Word w where w.language.id = :languageId order by w.id");
        q.setParameter("languageId", languageId);
        q.setMaxResults(1);
        q.setFirstResult(offset);
        Object o = q.getSingleResult();
        if (o == null) {
            return null;
        }
        return (Word) o;
    }

    @Override
    public Word loadWordForLanguageAndTag(long languageId, long tagId, int offset) {
        Query q = em
                .createQuery("select w from Word w join w.tags t where t.id = :tagId and w.language.id = :languageId order by w.id");
        q.setParameter("languageId", languageId);
        q.setParameter("tagId", tagId);
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

    @SuppressWarnings("unchecked")
    // TODO maybe change the return type to Iterable,....
    @Override
    public List<Word> load(long indexStart, int pageSize) {
        Query q = em.createQuery("from Word w where w.id >= :wordId order by w.id");
        q.setParameter("wordId", indexStart);
        q.setMaxResults(pageSize);
        return (List<Word>) q.getResultList();
    }

    @Override
    public Long findMaxWordId() {
        Query q = em.createQuery("select max(id) from Word");
        Object o = q.getSingleResult();
        if (o == null) {
            return null;
        }
        return (Long) o;
    }

}
