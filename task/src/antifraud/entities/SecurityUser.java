package antifraud.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class SecurityUser implements UserDetails {

    private final UserEntity userEntity;

    public SecurityUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().name()));
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !userEntity.isAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !userEntity.isAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !userEntity.isAccountExpired();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isAccountEnabled();
    }
}
