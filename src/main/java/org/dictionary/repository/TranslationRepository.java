package org.dictionary.repository;

import org.dictionary.domain.Translation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Translation entity.
 */
public interface TranslationRepository extends JpaRepository<Translation,Long> {

}
