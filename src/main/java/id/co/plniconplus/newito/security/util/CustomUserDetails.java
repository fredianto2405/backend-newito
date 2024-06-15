package id.co.plniconplus.newito.security.util;

import id.co.plniconplus.newito.user.dto.UserSessionDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private UserSessionDto userSession;

    public CustomUserDetails(UserSessionDto userSession) {
        this.userSession = userSession;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userSession.getPassword();
    }

    @Override
    public String getUsername() {
        return userSession.getUsername();
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
}
