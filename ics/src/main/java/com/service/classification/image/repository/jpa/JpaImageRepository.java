package com.service.classification.image.repository.jpa;

import com.service.classification.image.data.Image;
import com.service.classification.image.data.User;

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
