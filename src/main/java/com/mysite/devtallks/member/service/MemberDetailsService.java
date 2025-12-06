package com.mysite.devtallks.member.service;

import com.mysite.devtallks.member.entity.Member;
import com.mysite.devtallks.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * Spring Security가 로그인 시 호출하는 메서드.
     * 여기서는 이메일을 username으로 사용한다.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(usernameOrEmail)
                .orElseGet(() -> memberRepository.findByUsername(usernameOrEmail)
                        .orElseThrow(() -> new UsernameNotFoundException("No user: " + usernameOrEmail)));

        return toUserDetails(member);
    }

    private UserDetails toUserDetails(Member member) {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(member.getRole().roleName()));
        return User.builder()
                .username(member.getEmail()) // authentication principal로 email 사용
                .password(member.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
