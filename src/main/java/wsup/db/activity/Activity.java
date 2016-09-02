package wsup.db.activity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

/**
 * Describe an activity done by an user.
 *
 * @author xdai2
 * @since May 7, 2015
 *
 */
@Table
public class Activity {

	@PrimaryKey
	private ActivityPrimaryKey pkey;	

	@Column
	private String content;
	
	public Activity() {}
	
	public Activity(String username, long datetime, String type) {
		this(username, "", datetime, type);
	}
	
	public Activity(String username, String targetedUser, long datetime, String type) {
		pkey = new ActivityPrimaryKey(username, targetedUser, datetime, type);
	}
	 
	public ActivityPrimaryKey getPkey() {
		return pkey;
	}
	
	public void setPkey(ActivityPrimaryKey pkey) {
		this.pkey = pkey;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}	

	@Override
	public String toString() {
		return (pkey.getUsername() + ": " + pkey.getDatetime() + ", " + content);
	}
	
	@PrimaryKeyClass
	public static class ActivityPrimaryKey implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 5539812875139351620L;

		@PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
		private String username;
		
		@PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.PARTITIONED)
		private String type;	
		
		@PrimaryKeyColumn(ordinal = 2, type = PrimaryKeyType.PARTITIONED, name="targeted_user")
		private String targetedUser;

		@PrimaryKeyColumn(ordinal = 3, type = PrimaryKeyType.PARTITIONED)
		private Date datetime;	
		
		public ActivityPrimaryKey() {}
		
		public ActivityPrimaryKey(String username, String targetedUser, long datetime, String type) {
			this.username = username;
			this.targetedUser = targetedUser;
			this.datetime = new Date(datetime);
			this.type = type;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public Date getDatetime() {
			return datetime;
		}

		public void setDatetime(Date datetime) {
			this.datetime = datetime;
		}

		public String getTargetedUser() {
			return targetedUser;
		}

		public void setTargetedUser(String targetedUser) {
			this.targetedUser = targetedUser;
		}
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	}
}
