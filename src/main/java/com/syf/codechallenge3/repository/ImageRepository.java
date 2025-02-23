package com.syf.codechallenge3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.syf.codechallenge3.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
