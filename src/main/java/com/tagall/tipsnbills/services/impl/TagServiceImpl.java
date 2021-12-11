package com.tagall.tipsnbills.services.impl;

import com.tagall.tipsnbills.module.Project;
import com.tagall.tipsnbills.module.Tag;
import com.tagall.tipsnbills.module.User;
import com.tagall.tipsnbills.repo.ProjectRepository;
import com.tagall.tipsnbills.repo.TagRepository;
import com.tagall.tipsnbills.repo.UserRepository;
import com.tagall.tipsnbills.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public List<Tag> findAllTagByProject(Project project) {
        return tagRepository.findByProjectList_Id(project.getId());
    }

    @Override
    public List<Tag> findAllTagByUser(User user) {
        return tagRepository.findByUserList_Id(user.getId());
    }

    @Override
    public List<Tag> findAllTagByCategoryIsNull() {
        return tagRepository.findByCategoryIsNull();
    }

    @Override
    public List<Tag> findAllByCategoryNameContains(String name) {
        return tagRepository.findByCategoryNameContains(name);
    }

    @Override
    public Optional<Tag> findTagById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public boolean existsTagByName(String name) {
        return tagRepository.existsByName(name);
    }

    @Override
    public void deleteTagFromUsername(String tagName, String username) {

    }

    @Override
    public void deleteTagFromProject(String tagName, Long id) {

    }
}
