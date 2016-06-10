package org.dictionary.repository;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.dictionary.domain.QuizResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

@Named
public class DefaultQuizResultRepositoryCustom {

    private final EntityManager em;

    @Autowired
    public DefaultQuizResultRepositoryCustom(JpaContext jpaContext) {
        this.em = jpaContext.getEntityManagerByManagedType(QuizResult.class);
    }

    @SuppressWarnings("unchecked")
    public List<QuizResult> find(Long userId, LocalDate startDate, LocalDate endDate) {
        Query query = em
                .createQuery("from QuizResult where user.id = :userId and date >= :startDate and date <= :endDate");
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }
}
