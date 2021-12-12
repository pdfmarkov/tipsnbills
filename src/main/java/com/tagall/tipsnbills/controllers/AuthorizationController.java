package com.tagall.tipsnbills.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import com.tagall.tipsnbills.exceptions.ResourceIsAlreadyExistsException;
import com.tagall.tipsnbills.exceptions.ResourceIsNotValidException;
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
import com.tagall.tipsnbills.services.EmailService;
import com.tagall.tipsnbills.services.impl.EmailServiceImpl;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
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

import static org.springframework.beans.MethodInvocationException.ERROR_CODE;

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

    @Autowired
    EmailService emailService;

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

        String username = signUpRequest.getUsername();

        if (username == null) {
            throw new ResourceIsNotValidException("Username or password should not be empty");
        }

        if (organizationRepository.existsByUsername(signUpRequest.getUsername()))
            throw new ResourceIsAlreadyExistsException("Error: Username is already taken!");

        String password = generatePassayPassword();

        Organization organization = new Organization(signUpRequest.getUsername(),
                encoder.encode(password),
                signUpRequest.getUsername(),
                signUpRequest.getPhone_number(),
                signUpRequest.getName_organization(),
                null,
                true);

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

        emailService.sendSimpleMessage(signUpRequest.getUsername(),
                "Подтверждение почты",
                "Компания Tips&Bills, благодарим вас за подключение нашей платформы!\n" +
                        "Ваш логин: " + signUpRequest.getUsername() + "\n" +
                        "Ваш пароль: " + password + "\n" +
                        "Ссылка на личный кабинет: "); //TODO написать ссылку

        return ResponseEntity.ok(new MessageResponseDto("User registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequestDto logOutRequest) {
        refreshTokenService.deleteByOrganizationId(logOutRequest.getUserId());
        return ResponseEntity.ok(new MessageResponseDto("Log out successful!"));
    }

    private String generatePassayPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return ERROR_CODE;
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        String password = gen.generatePassword(10, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
        return password;
    }
}