package com.yww.nexus.security;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * <p>
 *      自定义的UserDetails类
 * </p>
 *
 * @author  yww
 * @since  2023/12/5
 */
@Builder
public class AccountUser implements UserDetails {

    /**
     * 用户ID（UserDetails中没有的，自定义添加）
     */
    private final String userId;

    /**
     * 用户密码
     */
    private final String password;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 用户权限集合，不能为null
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 用户是否已过期，过期的用户无法认证（暂无认证，全部置于true）<br/>
     * true表示没有过期，false表示用户过期
     */
    private final boolean accountNonExpired;

    /**
     * 用户是否已被锁定，被锁定的用户无法认证（暂无用户，全部置于true）<br/>
     * true表示没有被锁定，false表示用户被锁定
     */
    private final boolean accountNonLocked;

    /**
     * 用户的凭证或密码是否已过期，过期的凭证无法认证（暂无用处，全部置于true）<br/>
     * true表示没有过期，false表示过期
     */
    private final boolean credentialsNonExpired;

    /**
     * 用户是否已被禁用，被禁用的用户不能登录身份认证  <br/>
     * true表示没被禁用，false表示已经被禁用
     */
    private final boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
        return this.enabled;
    }

}
