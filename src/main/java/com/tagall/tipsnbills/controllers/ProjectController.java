package com.tagall.tipsnbills.controllers;

import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.module.Project;
import com.tagall.tipsnbills.module.Tag;
import com.tagall.tipsnbills.module.UserProject;
import com.tagall.tipsnbills.module.requested.IdRequestDto;
import com.tagall.tipsnbills.module.requested.InvitationRequestDto;
import com.tagall.tipsnbills.module.requested.ProjectRequestDto;
import com.tagall.tipsnbills.module.responses.*;
import com.tagall.tipsnbills.services.ProjectService;
import com.tagall.tipsnbills.services.TagService;
import com.tagall.tipsnbills.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins = {"https://teambuilderproject-web.herokuapp.com/",
//        "http://localhost:8081/"
})
@RestController
@Log4j2
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    @Autowired
    TagService tagService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllProjects(@RequestParam(required = false) String name) {
        List<Project> projects;
        if (name == null || name.trim().equals("")) projects = projectService.getAllProject();
        else projects = projectService.getAllProjectWithName(name);
        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projects);
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");
        return ResponseEntity.ok(projectListResponseDto);
    }


    @GetMapping("/get/all/page")
    public ResponseEntity<?> getAllProjectsWithPage(@RequestParam(required = false) String name, @RequestParam() Integer page, @RequestParam() Integer size) {
        List<Project> projects;
        if (name == null || name.trim().equals("")) projects = projectService.getAllProjectPageable(page, size).toList();
        else projects = projectService.getAllProjectWithNamePageable(name, page, size);
        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projects);
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");
        return ResponseEntity.ok(projectListResponseDto);
    }

    @GetMapping("/get/all/tag")
    public ResponseEntity<?> getAllProjectsWithTagAndPage(@RequestParam(required = false) String tagName, @RequestParam() Integer page, @RequestParam() Integer size) {
        List<Project> projects;
        if (tagName == null || tagName.trim().equals("")) projects = projectService.getAllProjectPageable(page, size).toList();
        else projects = projectService.getAllProjectWithTagPageable(tagName, page, size);
        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projects);
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");
        return ResponseEntity.ok(projectListResponseDto);
    }

    @Deprecated
    @GetMapping("/get/all/every")
    public ResponseEntity<?> getAllProjectsWithTagAndNameAndPage(@RequestParam(required = false) String name, @RequestParam(required = false) String tagName, @RequestParam() Integer page, @RequestParam() Integer size) {
        List<Project> projects;
        if (tagName == null || tagName.trim().equals("") || name == null || name.trim().equals("")) projects = projectService.getAllProjectPageable(page, size).toList();
        else projects = projectService.getAllProjectWithTagAndNamePageable(name, tagName, page, size);
        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projects);
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");
        return ResponseEntity.ok(projectListResponseDto);
    }

    @GetMapping("/get/all/custom")
    public ResponseEntity<?> getAllProjectsWithCustomParams
            (@RequestParam(required = false) String name,
             @RequestParam(required = false) List<String> tagNameList,
             @RequestParam(required = false) Integer page,
             @RequestParam(required = false) Integer size) {
        Set<Project> projects = new HashSet<>();
        fillProjectsWithCustomParams(name, tagNameList, page, size, projects);
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");
        if (tagNameList != null && !tagNameList.isEmpty())
            deleteSameTags(tagNameList, projects);
        List<Project> projectList = new ArrayList<>(projects);
        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projectList);
        return ResponseEntity.ok(projectListResponseDto);
    }

    @GetMapping("/get/user")
    public ResponseEntity<?> getAllProjectsByUser() {

        Set<Project> projects = new HashSet<>(projectService.getAllProjectsByUser(
                userService.findUserByUsername(getCurrentUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"))));
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");

        List<Project> projectList = new ArrayList<>(projects);

        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projectList);

        return ResponseEntity.ok(projectListResponseDto);
    }

    @GetMapping("/get/profile")
    public ResponseEntity<?> getAllProjectsWithConnections() {

        Set<Project> projects = new HashSet<>(projectService.getAllProjectsByUser(
                userService.findUserByUsername(getCurrentUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"))));
        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects not found");

        List<Project> projectList = new ArrayList<>(projects);

        ProjectListResponseDto projectListResponseDto = getProjectListResponseDto(projectList);

        ProfileProjectsResponseDto profileProjectsResponseDto = new ProfileProjectsResponseDto();

        profileProjectsResponseDto.setProjectListResponseDto(projectListResponseDto);

        HashMap<Long, String> connections = new HashMap<>();
        for (Project project : projectList)
            for (UserProject userProject : project.getUserProjectList()) {
                if (userProject.getUser().getUsername().equals(getCurrentUsername())) {
                    if (userProject.getIsAccepted() == null)
                        connections.put(project.getId(), "Still waiting for answer from leader of the project...");
                    else if (userProject.getIsAccepted())
                        connections.put(project.getId(), project.getConnect());
                    else if (!userProject.getIsAccepted())
                        connections.put(project.getId(), "The leader rejected your application ;(");
                }
            }

        profileProjectsResponseDto.setConnections(connections);

        return ResponseEntity.ok(profileProjectsResponseDto);
    }

    @GetMapping("/get/number")
    public ResponseEntity<?> getNumberOfValues() {
        return ResponseEntity.ok(projectService.getNumberOfProject());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getProject(@PathVariable("id") long id) {
        return ResponseEntity.ok(getProjectListResponseDto(List.of(projectService.findProjectById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Project Not Found")))));
    }

    @GetMapping("/get/notifications")
    public ResponseEntity<?> getNotifications() {

        // Check users who want to be part of your Projects

        Set<Project> projects = new HashSet<>(projectService.getAllProjectsByUser(
                userService.findUserByUsername(getCurrentUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found"))));

        if (projects.isEmpty()) throw new ResourceNotFoundException("Error: Projects Not Found");

        return ResponseEntity.ok(getNotificationResponseDto(projects));
    }

    @PostMapping("/add/project")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> addNewProject(@Valid @RequestBody ProjectRequestDto projectRequestDto) {

        Project project = new Project(projectRequestDto.getName(),
                projectRequestDto.getStart_date(),
                projectRequestDto.getEnd_date(),
                projectRequestDto.getDescription(),
                projectRequestDto.getConnect(),
                new UserProject(userService.findUserByUsername(getCurrentUsername())
                        .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found")),
                        true, true));

        return ResponseEntity.ok(getProjectResponseDto(projectService.saveProject(project)));
    }

    @PostMapping("/add/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> addUserToProject(@Valid @RequestBody IdRequestDto idRequestDto) {

        Project project = projectService.findProjectById(idRequestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Project Not Found"));

        List<UserProject> userProjectList = project.getUserProjectList();

        String leaderUsername = "";

        if (!userProjectList.isEmpty())
            for (UserProject userProject : userProjectList) {

                if (userProject.isLeader()) leaderUsername = userProject.getUser().getUsername();

                if (userProject.getUser().getUsername().equals(getCurrentUsername())) {
                    if (userProject.isLeader())
                        return ResponseEntity.ok(new MessageResponseDto("It's your own project! P.S. And it's amazing <3"));
                    if (userProject.getIsAccepted() == null)
                        return ResponseEntity.ok(new MessageResponseDto("Still wait the answer from the leader of the project!"));
                    if (userProject.getIsAccepted())
                        return ResponseEntity.ok(new MessageResponseDto("You already taken part in this project!"));
                    if (!userProject.getIsAccepted())
                        return ResponseEntity.ok(new MessageResponseDto("Leader didn't allow you to be part of this project."));
                }
            }

        userProjectList.add(new UserProject(userService.findUserByUsername(getCurrentUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Error: User Not Found")),
                null, false, project));
        projectService.saveProject(project);

        try {
            notificationDispatcher.sendToUser(
                    leaderUsername,
                    "/notification/item",
                    new NotificationDispatcher.Notification("ADD")
            );
        } catch (NotificationDispatcher.NotificationException e) {
            log.warn("This user is currently not available");
        }

        return ResponseEntity.ok(new MessageResponseDto("Well done! Wait for the answer from the leader of the project!"));
    }

    @PostMapping("/invite")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> changeInvitation(@Valid @RequestBody InvitationRequestDto invitationRequestDto){

        Project project = projectService.findProjectById(invitationRequestDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Error: Project Not Found"));

        List<UserProject> userProjectList = project.getUserProjectList();

        for (UserProject userProject : userProjectList)
            if (userProject.getId().equals(invitationRequestDto.getUserProjectId()))
                userProject.setIsAccepted(invitationRequestDto.isAccepted());

        projectService.saveProject(project);

//        notificationDispatcher.sendToUser(
//                invitationRequestDto.getUsername(),
//                "/notification/item",
//                new NotificationDispatcher.Notification("INVITATION " + invitationRequestDto.isAccepted())
//        );

        return ResponseEntity.ok(new MessageResponseDto("Change status of invitation"));
    }

    //Send STOMP message to /swns/start to begin a STOMP WSbased connection.
    @MessageMapping("/start")
    public void send(StompHeaderAccessor stompHeaderAccessor) {

        //Can get Spring Principal/Session user from the STOMP header (awesome!).
        final Principal user = stompHeaderAccessor.getUser();
        log.info("{} initiated a STOMP based websocket.", user != null ? user.getName() : "ANON");
        //Add the user's principal name as the key and their STOMP session Id to the static vol HashMap<String,String> in
        //the NotificationDispatcher.
        NotificationDispatcher.getPrincipalNameToSockSessionMap().put(user.getName(), stompHeaderAccessor.getSessionId());
    }

    private String getCurrentUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    private ProjectResponseDto getProjectResponseDto(Project project) {
        String leaderEmail = "";
        long leaderId = 0;
        List<String> tagNameList;

        for (UserProject userProject : project.getUserProjectList()) {
            if (userProject.isLeader()) {
                leaderEmail = userProject.getUser().getUsername();
                leaderId = userProject.getUser().getId();
            }
        }

        tagNameList = new ArrayList<>();

        if (project.getTagList() != null)
            for (Tag tag : project.getTagList()) tagNameList.add(tag.getName());


        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getStart_date(),
                project.getEnd_date(),
                project.getDescription(),
                project.getImage(),
                leaderEmail,
                leaderId,
                tagNameList
        );
    }

    private ProjectListResponseDto getProjectListResponseDto(List<Project> projects) {
        ProjectListResponseDto projectListResponseDto = new ProjectListResponseDto();
        List<ProjectResponseDto> projectResponseDtoList = new ArrayList<>();
        for (Project project : projects)
            projectResponseDtoList.add(getProjectResponseDto(project));
        projectListResponseDto.setProjects(projectResponseDtoList);
        projectListResponseDto.setNumberOfProjects(projectResponseDtoList.size());
        return projectListResponseDto;
    }

    // Delete All Projects Which Don't Have All Tags From TagNameList
    private void deleteSameTags(@RequestParam(required = false) List<String> tagNameList, Set<Project> projects) {
        List<Tag> tagList = new ArrayList<>();
        for (String tagName : tagNameList)
            tagList.add(tagService.findTagByName(tagName)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Tag Not Found")));
        projects.removeIf(project -> !project.getTagList().containsAll(tagList));
    }

    private void fillProjectsWithCustomParams(String name, List<String> tagNameList, Integer page, Integer size, Set<Project> projects) {
        if (page == null && size == null && (tagNameList == null || tagNameList.isEmpty()) && (name == null || name.trim().equals(""))) {
            projects.addAll(projectService.getAllProject());
        } else if ((tagNameList == null || tagNameList.isEmpty()) && (name == null || name.trim().equals(""))) {
            projects.addAll(projectService.getAllProjectPageable(page, size).toList());
        } else if (page == null && size == null && (name == null || name.trim().equals(""))) {
            projects.addAll(projectService.getAllProjectsWithTagsAndNamePageable("", tagNameList, 0, Math.toIntExact(projectService.getNumberOfProject())));
        } else if (name == null || name.trim().equals("")) {
            projects.addAll(projectService.getAllProjectsWithTagsAndNamePageable("", tagNameList, page, size));
        } else if (page == null && size == null && (tagNameList == null || tagNameList.isEmpty())) {
            projects.addAll(projectService.getAllProjectWithNamePageable(name, 0, Math.toIntExact(projectService.getNumberOfProject())));
        } else if ((tagNameList == null || tagNameList.isEmpty())) {
            projects.addAll(projectService.getAllProjectWithNamePageable(name, page, size));
        } else if (page == null && size == null ) {
            projects.addAll(projectService.getAllProjectWithNamePageable(name, 0, Math.toIntExact(projectService.getNumberOfProject())));
        } else {
            projects.addAll(projectService.getAllProjectsWithTagsAndNamePageable(name, tagNameList, page, size));
        }
    }

    private NotificationResponseDto getNotificationResponseDto(Set<Project> projects) {
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();

        List<CheckUserAsLeaderResponseDto> checkUserAsLeaderResponseDtoList = new ArrayList<>();
        List<UserProject> userProjectList;
        List<String> userTagNameList;
        String leaderEmail = "";

        for (Project project : projects) {
            userProjectList = project.getUserProjectList();
            for (UserProject userProject : userProjectList)
                if (userProject.isLeader()) leaderEmail = userProject.getUser().getUsername();
            if (leaderEmail.equals(getCurrentUsername()))
                for (UserProject userProject : userProjectList)
                    if (userProject.getIsAccepted() == null) {
                        userTagNameList = new ArrayList<>();
                        if (userProject.getUser().getTagList() != null) for (Tag tag : userProject.getUser().getTagList()) userTagNameList.add(tag.getName());
                        checkUserAsLeaderResponseDtoList.add(
                                new CheckUserAsLeaderResponseDto(
                                        userProject.getId(),
                                        getProjectResponseDto(project),
                                        userProject.getUser().getId(),
                                        userProject.getUser().getUsername(),
                                        userProject.getUser().getDescription(),
                                        userTagNameList

                                )
                        );
                    }
        }

        notificationResponseDto.setCheckUserAsLeaderResponseDtoList(checkUserAsLeaderResponseDtoList);
        return notificationResponseDto;
    }

}
