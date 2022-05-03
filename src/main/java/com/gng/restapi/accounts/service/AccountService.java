package com.gng.restapi.accounts.service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gng.restapi.accounts.model.Account;
import com.gng.restapi.accounts.model.AccountRole;
import com.gng.restapi.accounts.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {
	
	private final AccountRepository accountRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = accountRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		
		return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
	}

	private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
				.collect(Collectors.toSet());
	}
	
}
