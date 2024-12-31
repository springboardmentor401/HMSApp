package com.hms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hms.entities.UserInfo;

public interface UserRepository extends JpaRepository<UserInfo, String>  {

	UserInfo findByUserNameAndPassword(String userName, String password);
	UserInfo findByUserName(String userName);
}
