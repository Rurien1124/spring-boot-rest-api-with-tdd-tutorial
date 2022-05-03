package com.gng.restapi.accounts.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gng.restapi.accounts.model.Account;
import com.gng.restapi.accounts.model.AccountRole;
import com.gng.restapi.accounts.repository.AccountRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class AccountServiceTest {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Nested
	@DisplayName("사용자 조회")
	class QueryUser {
		@Test
		@DisplayName("성공")
		public void ok() {
			// Given
			String email = "test@email.com";
			String password = "testPwd";
			Account account = Account.builder()
					.email(email)
					.password(password)
					.roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
					.build();
			
			accountRepository.save(account);
			
			// When
			UserDetails userDetails = accountService.loadUserByUsername(email);
			
			// Then
			assertThat(userDetails.getPassword()).isEqualTo(password);
		}
		
		@Test
		@DisplayName("실패")
		public void failure() {
			// Given
			String email = "test@email.com";

			// When
			try {
				accountService.loadUserByUsername(email);
				
				fail("supposed to be failed");
			// Then
			} catch(UsernameNotFoundException uex) {
				assertThat(uex.getMessage()).containsSequence(email);
			}
		}
	}
}
