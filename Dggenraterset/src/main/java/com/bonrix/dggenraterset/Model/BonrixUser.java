package com.bonrix.dggenraterset.Model;

import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.userdetails.User;

@SuppressWarnings({"rawtypes","unchecked"})
public class BonrixUser  extends User{
	private static final long serialVersionUID = -3531439484732724601L;

	private Long userId;
	
	private Set<UserRole> userRole;
	public Set<UserRole> getUserRole() {
		return userRole;
	}


	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}


	public BonrixUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection authorities,Long userId,Set<UserRole> userRole) {

		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.userId=userId;
		this.userRole=userRole;
	}

	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
