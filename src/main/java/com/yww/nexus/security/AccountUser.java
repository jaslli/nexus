package com.yww.nexus.security;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * 用户ID
     */
    @Getter
    @Setter
    private Integer userid;
    /**
     * username
     */
    @Setter
    private String userName;
    /**
     * nickname
     */
    @Getter
    @Setter
    private String nickName;
    /**
     * password
     */
    @Setter
    private String password;

    /**
     * enabled
     */
    @Setter
    private boolean enabled;

    /**
     * 用户权限集合，不能为null
     */
    @Getter
    @Setter
    private List<String> permissions;

    /**
     * 角色
     */
    @Getter
    @Setter
    private List<String> roles;

    @Override
    public Collection<Authority> getAuthorities() {
        List<Authority> authorities = new ArrayList<>();
        // 角色权限
        if (CollectionUtil.isNotEmpty(this.roles)) {
            authorities.addAll(this.roles.stream().map(Authority::new).toList());
        }
        //  菜单权限
        if (CollectionUtil.isNotEmpty(this.permissions)) {
            authorities.addAll(this.permissions.stream().map(Authority::new).toList());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
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
