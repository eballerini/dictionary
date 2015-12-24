package org.dictionary.repository;

import org.dictionary.domain.Language;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Language entity.
 */
public interface LanguageRepository extends JpaRepository<Language,Long> {

}
