package com.ead.authuser.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ead.authuser.repositories.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;

public class UserCourseServiceImpl implements UserCourseService {
	
	@Autowired
	UserCourseRepository userCourseRepository;

}
