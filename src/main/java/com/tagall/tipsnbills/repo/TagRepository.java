package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    Optional<Tag> findById(Long id);

    List<Tag> findByProjectList_Id(Long id);

    List<Tag> findByUserList_Id(Long id);

    List<Tag> findByCategoryIsNull();

    List<Tag> findByCategoryNameContains(String name);

    boolean existsByName(String name);

}
