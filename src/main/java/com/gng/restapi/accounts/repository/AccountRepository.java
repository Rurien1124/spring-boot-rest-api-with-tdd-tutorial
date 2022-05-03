package com.gng.restapi.accounts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gng.restapi.accounts.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

	public Optional<Account> findByEmail(String username);

}
