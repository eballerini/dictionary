package org.dictionary.repository;

import org.dictionary.domain.Tag;

public interface TagRepositoryCustom {

    Tag findByName(String name);

}