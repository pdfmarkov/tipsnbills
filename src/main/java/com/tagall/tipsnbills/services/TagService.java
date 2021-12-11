package com.tagall.tipsnbills.services;

import com.tagall.tipsnbills.module.Project;
import com.tagall.tipsnbills.module.Tag;
import com.tagall.tipsnbills.module.User;

import java.util.List;
import java.util.Optional;

public interface TagService {

    List<Tag> findAllTagByProject(Project project);

    List<Tag> findAllTagByUser(User user);

    List<Tag> findAllTagByCategoryIsNull();

    List<Tag> findAllByCategoryNameContains(String name);

    Optional<Tag> findTagById(Long id);

    Optional<Tag> findTagByName(String name);

    Tag saveTag(Tag tag);

    boolean existsTagByName(String name);

    void deleteTagFromUsername(String tagName, String username);

    void deleteTagFromProject(String tagName, Long id);

}
