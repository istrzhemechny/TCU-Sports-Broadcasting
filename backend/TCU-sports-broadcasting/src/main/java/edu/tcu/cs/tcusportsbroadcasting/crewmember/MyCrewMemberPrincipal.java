package edu.tcu.cs.tcusportsbroadcasting.crewmember;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyCrewMemberPrincipal implements UserDetails {

    private CrewMember crewMember;

    public MyCrewMemberPrincipal(CrewMember crewMember) {
        this.crewMember = crewMember;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.crewMember.getRole()));
    }

    @Override
    public String getPassword() {
        return this.crewMember.getPassword();
    }

    @Override
    public String getUsername() {
        return this.crewMember.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public CrewMember getCrewMember() {
        return crewMember;
    }

}
