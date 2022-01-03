package com.nyf.mvc.config;

import com.nyf.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;

    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        super(originalAdmin.getUserName(), originalAdmin.getUserPswd(), authorities);

        this.originalAdmin = originalAdmin;

        this.originalAdmin.setUserPswd(null);
    }
    public Admin getOriginalAdmin(){
        return originalAdmin;
    }
}
