package com.bonrix.dggenraterset.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bonrix.dggenraterset.Model.BonrixUser;
import com.bonrix.dggenraterset.Model.UserRole;
import com.bonrix.dggenraterset.Repository.UserRepository;


@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userDao;

	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
	
		com.bonrix.dggenraterset.Model.User user = userDao.findByUserName(username);
		
		List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRole());
	//	System.out.println(user.getUserRole());
		return buildUserForAuthentication(user, authorities);
		
	}

	// Converts com.bonrix.RasbarypimavenProject.Model.User user to
	// org.springframework.security.core.userdetails.User
	private BonrixUser buildUserForAuthentication(com.bonrix.dggenraterset.Model.User user, List<GrantedAuthority> authorities) {
		return new BonrixUser(user.getUsername(), user.getPassword(), user.isEnabled(), true, true, true, authorities,user.getId(),user.getUserRole());
	}

	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (UserRole userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
		}
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
		return Result;
	}

}