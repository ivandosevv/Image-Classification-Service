package com.vmware.talentboost.ics.repository.jpa;

import com.vmware.talentboost.ics.data.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaTagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> getTagByName(String name);
    Optional<Tag> getTagById(Integer id);
}
