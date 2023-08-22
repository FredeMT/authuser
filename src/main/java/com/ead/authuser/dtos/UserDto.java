package com.ead.authuser.dtos;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import validation.UsernameConstraint;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	
	//Definicao da interface para diferentes visões.
	public interface UserView {
		//Visão referente ao método registerUser() para cadastrar dados básicos do user
		public static interface RegistrationPost {}
		//Visão referente a atualizacao dos dados básico do user.
		public static interface UserPut {}
		//Visão referente a atualizacao do password.
		public static interface PasswordPut {}
		//Visão referente a atualizacao da imagem de perfil do user.
		public static interface ImagePut {}
	}
	
	private UUID userId;
	
	//@NotBlank(groups = UserView.RegistrationPost.class)
	@Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
	@UsernameConstraint(groups = UserView.RegistrationPost.class)
	@JsonView(UserView.RegistrationPost.class)
	private String username;
	
	@NotBlank(groups = UserView.RegistrationPost.class)
	@Email(groups = UserView.RegistrationPost.class)
	@JsonView(UserView.RegistrationPost.class)
	private String email;
	
	@NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
	@Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
	@JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
	private String password;
	
	@NotBlank(groups = UserView.PasswordPut.class)
	@Size(min = 6, max = 20, groups = UserView.PasswordPut.class)
	@JsonView(UserView.PasswordPut.class)
	private String oldPassword;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String fullName;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String phoneNumber;
	
	@JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String cpf;	
	
	@NotBlank(groups = UserView.ImagePut.class)
	@JsonView(UserView.ImagePut.class)
	private String imageUrl;

}
