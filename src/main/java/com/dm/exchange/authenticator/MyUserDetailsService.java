package com.dm.exchange.authenticator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.owasp.esapi.contrib.spring.authenticator.AuthenticatedUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

import com.dm.Subject;

public class MyUserDetailsService extends JdbcDaoImpl{
   
    protected List<UserDetails> loadUsersByUsername(String username) {
        return getJdbcTemplate().query(this.getUsersByUsernameQuery(), new String[] {username}, new RowMapper<UserDetails>() {
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong(1);
                String accountName = rs.getString(2);
                String password = rs.getString(3);
                long ed = rs.getLong(4);
                Date expirationDate = new Date(ed);
                int count = rs.getInt(5);
                String remoteHost = rs.getString(6);
                Date lastFailedLoginTime = rs.getDate(7);
                Date lastLoginTime = rs.getDate(8);
                Date lastPasswordChangeTime = rs.getDate(9);
                String screenName = rs.getString(10);                
                boolean enabled = rs.getBoolean(11);
                boolean locked = rs.getBoolean(12);
                
                Subject subject = new Subject(accountName);
                subject.setAccountId(id);
                subject.setExpirationDate(expirationDate);
                subject.setFailedLoginCount(count);
                subject.setLastHostAddress(remoteHost);
                subject.setLastFailedLoginTime(lastFailedLoginTime);
                subject.setLastLoginTime(lastLoginTime);
                subject.setLastPasswordChangeTime(lastPasswordChangeTime);
                subject.setScreenName(screenName);
                subject.setEnabled(enabled);
                subject.setLocked(locked);
                
                AuthenticatedUser authenticatedUser = new AuthenticatedUser(subject);
                return authenticatedUser;
            }
        });
    }

	@Override
	protected String getRolePrefix() {
		return "ROLE_";
	}
	
}
