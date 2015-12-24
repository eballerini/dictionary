package org.dictionary.repository;

import org.dictionary.domain.Word;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Word entity.
 */
public interface WordRepository extends JpaRepository<Word,Long> {

}
