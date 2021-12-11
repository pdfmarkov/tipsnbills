package com.tagall.tipsnbills.repo;

import com.tagall.tipsnbills.module.Project;
import com.tagall.tipsnbills.module.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findById(Long id);

    List<Project> findByNameContainingOrderById(String name);

    List<Project> findByUserProjectListUserOrderById(User user);

}
