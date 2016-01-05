package org.dictionary.repository;

import org.dictionary.domain.FileImport;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FileImport entity.
 */
public interface FileRepository extends JpaRepository<FileImport,Long> {

}
