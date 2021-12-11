package com.tagall.tipsnbills.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.tagall.tipsnbills.exceptions.ResourceIsAlreadyExistsException;
import com.tagall.tipsnbills.exceptions.ResourceNotFoundException;
import com.tagall.tipsnbills.exceptions.TokenRefreshException;
import com.tagall.tipsnbills.module.ERole;
import com.tagall.tipsnbills.module.Organization;
import com.tagall.tipsnbills.module.RefreshToken;
import com.tagall.tipsnbills.module.Role;
import com.tagall.tipsnbills.module.requested.LogOutRequestDto;
import com.tagall.tipsnbills.module.requested.LoginRequestDto;
import com.tagall.tipsnbills.module.requested.RefreshTokenRequestDto;
import com.tagall.tipsnbills.module.requested.SignupRequestDto;
import com.tagall.tipsnbills.module.responses.JwtResponseDto;
import com.tagall.tipsnbills.module.responses.MessageResponseDto;
import com.tagall.tipsnbills.module.responses.TokenRefreshResponseDto;
import com.tagall.tipsnbills.repo.RoleRepository;
import com.tagall.tipsnbills.repo.OrganizationRepository;
import com.tagall.tipsnbills.security.module.UserDetailsImpl;
import com.tagall.tipsnbills.security.jwt.JwtUtils;
import com.tagall.tipsnbills.security.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TODO: Comment localhost before commit
//@CrossOrigin(origins = {"https://teambuilderproject-web.herokuapp.com/",
//        "http://localhost:8081/"
//})
@CrossOrigin("*") // TODO : DELETE
@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponseDto(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), roles));

    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody RefreshTokenRequestDto request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getOrganization)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponseDto(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDto signUpRequest) {

        if (organizationRepository.existsByUsername(signUpRequest.getUsername()))
            throw new ResourceIsAlreadyExistsException("Error: Username is already taken!");

        Organization organization = new Organization(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getLogin_name(),
                signUpRequest.getPhone_number(),
                signUpRequest.getName_organization(),
                signUpRequest.getAgreement(),
                signUpRequest.isState());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        organization.setRoles(roles);
        organizationRepository.save(organization);

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequestDto logOutRequest) {
        refreshTokenService.deleteByOrganizationId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponseDto("Log out successful!"));
    }
}