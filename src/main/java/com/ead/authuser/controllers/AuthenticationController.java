package com.ead.authuser.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dtos.JwtDto;
import com.ead.authuser.dtos.LoginDto;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.dtos.UserDto.UserView;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.models.RoleModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;
import com.ead.authuser.enums.UserType;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping("/signup")
	public ResponseEntity<Object> registerUser(@RequestBody @Validated(UserDto.UserView.RegistrationPost.class)
													@JsonView(UserView.RegistrationPost.class) UserDto userDto){
		log.debug("POST registerUser userDto received {}", userDto.toString());
		if(userService.existsByUserName(userDto.getUsername())) {
			log.warn("Username {} is already taken.", userDto.getUsername());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
		}
		if(userService.existsByEmail(userDto.getEmail())) {
			log.warn("Email {} is already taken.", userDto.getEmail());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
		}
		RoleModel roleModel = roleService.findByRoleName(RoleType.ROLE_STUDENT)
				.orElseThrow(() -> new RuntimeException("Error: Role is Not Found."));
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);
		userModel.setUserStatus(UserStatus.ACTIVE);
		userModel.setUserType(UserType.STUDENT);
		userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.getRoles().add(roleModel);
		userService.saveUser(userModel);
		log.debug("POST registerUser userModel saved {}", userModel.toString());
		log.info("User saved successfully - userId {}", userModel.getUserId());
		return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto){
		//No authenticationManager é feita a autenticacao passando login e senha, retornando o authentication object.
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		//Define (seta) no Spring Security Context o authentication object.
		SecurityContextHolder.getContext().setAuthentication(authentication);
		//Gera o JWT com os dados do authentication object definidos no Spring Security Context retornando uma string.
		String jwt = jwtProvider.generateJwt(authentication);
		//Retorna o JWT no body para o client.
		return ResponseEntity.ok(new JwtDto(jwt));
	}

	
	@GetMapping("/")
	public String index() {
		log.trace("TRACE");
		log.debug("DEBUG");
		log.info("INFO");
		log.warn("WARN");
		log.error("ERROR");
		/*
		try {
			throw new Exception("Exception message.");
		} catch (Exception e) {
			log.error("----ERROR----", e);
		}
		*/
		return "Logging Spring Boot...";
	}
}
