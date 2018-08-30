package com.revature.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.revature.model.Authorities;
import com.revature.model.User;

@Repository
@Transactional
public interface AuthoritiesRepository extends JpaRepository<Authorities, String>{
	List<Authorities> findByUser(User user);
}
