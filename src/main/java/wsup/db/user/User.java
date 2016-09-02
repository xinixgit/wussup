package wsup.db.user;

import java.util.Set;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

/**
 * An user of this app.
 *
 * @author xdai2
 * @since Apr 28, 2015
 *
 */
@Table(value="user")
public class User {

	@PrimaryKey
	private String username;
	
	@Column
	private String password;
	
	@Column(value="secure_token")
	private String secureToken;
	
	@Column
	private Set<String> friends;
	
	public User() {
		
	}
	
	public User(String username, String password, String secureToken) {
		this.username = username;
		this.password = password;
		this.secureToken = secureToken;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSecureToken() {
		return secureToken;
	}

	public void setSecureToken(String secureToken) {
		this.secureToken = secureToken;
	}

	public Set<String> getFriends() {
		return friends;
	}

	public void setFriends(Set<String> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return String.format("user (%s)", username);
	}
}
