package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.User;
import com.tagall.tipsnbills.module.requested.AddUserInfoRequestDto;
import com.tagall.tipsnbills.module.responses.UserResponseDto;
import com.tagall.tipsnbills.services.ProjectService;
import com.tagall.tipsnbills.services.TagService;
import com.tagall.tipsnbills.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"https://teambuilderproject-web.herokuapp.com/",
//        "http://localhost:8081/"
})
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TagService tagService;

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo() {
        User user = userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"));
        UserResponseDto userResponseDto = getUserResponseDto(user);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUserInfo(@Valid @RequestBody AddUserInfoRequestDto addUserInfoRequestDto) {

        User user = userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"));

        if (addUserInfoRequestDto.getName() != null && !addUserInfoRequestDto.getName().equals(""))
            user.setName(addUserInfoRequestDto.getName());
        if (addUserInfoRequestDto.getSurname() != null && !addUserInfoRequestDto.getSurname().equals(""))
            user.setSurname(addUserInfoRequestDto.getSurname());
        if (addUserInfoRequestDto.getBirth_date() != null && !addUserInfoRequestDto.getBirth_date().equals(""))
            user.setBirth_date(addUserInfoRequestDto.getBirth_date());
        if (addUserInfoRequestDto.getDescription() != null && !addUserInfoRequestDto.getDescription().equals(""))
            user.setDescription(addUserInfoRequestDto.getDescription());

        UserResponseDto userResponseDto = getUserResponseDto(userService.saveUser(user));

        return ResponseEntity.ok(userResponseDto);
    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    private UserResponseDto getUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setDescription(user.getDescription());
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setImage(user.getImage());
        userResponseDto.setName(user.getName());
        userResponseDto.setBirth_date(user.getBirth_date());
        userResponseDto.setSurname(user.getSurname());
        return userResponseDto;
    }
}
