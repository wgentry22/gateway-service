package com.revature.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {
	User findByUsernameAndPassword(String username, String password);
	User findByUsername(String username);
}
