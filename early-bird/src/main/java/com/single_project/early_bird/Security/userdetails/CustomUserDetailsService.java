package com.single_project.early_bird.Security.userdetails;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;


@Component("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = repository.findByEmail(username);
        User findUser = optionalUser.orElseThrow(() -> new BadRequestException("존재하지 않는 회원정보 입니다!"));

        log.info("로그인한 멤버의 멤버 ID " + findUser.getUserId().toString());

        Collection<? extends GrantedAuthority> authorities = findUser.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .toList();

        return new CustomUserDetails(findUser);
    }
}


