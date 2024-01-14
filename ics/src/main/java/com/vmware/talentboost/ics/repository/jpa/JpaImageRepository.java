package com.vmware.talentboost.ics.repository.jpa;

import com.vmware.talentboost.ics.data.Image;
import com.vmware.talentboost.ics.data.Tag;
import com.vmware.talentboost.ics.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaImageRepository extends JpaRepository<Image, Integer> {
    Optional<Image> getImageById(Integer id);
    Optional<Image> getImageByUrl(String url);

	List<Image> findByUser(User user);
}
