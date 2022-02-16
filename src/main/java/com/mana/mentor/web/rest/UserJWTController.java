package com.mana.mentor.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mana.mentor.domain.User;
import com.mana.mentor.repository.UserRepository;
import com.mana.mentor.responses.ResponseVM;
import com.mana.mentor.security.jwt.JWTFilter;
import com.mana.mentor.security.jwt.TokenProvider;
import com.mana.mentor.service.dto.AdminUserDTO;
import com.mana.mentor.web.rest.vm.LoginVM;
import javax.annotation.Resource;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Resource
    UserRepository userRepository;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    public ResponseEntity authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());

        User user = userRepository.findOneByLogin(loginVM.getUsername()).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        if (user != null) {
            user.setToken(jwt);
            AdminUserDTO adminUserDTO = new AdminUserDTO(user);
            //System.err.println(adminUserDTO);

            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(adminUserDTO, httpHeaders, HttpStatus.OK);
        }

        return new ResponseEntity<>(
            new ResponseVM(401, "Bad Credentials", "Please check your credentials and make sure they are correct."),
            httpHeaders,
            HttpStatus.resolve(500)
        );
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
