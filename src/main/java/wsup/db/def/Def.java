package wsup.db.def;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import def.util.StringsUtils;

/**
 * DAO object for defition.
 *
 * @author xdai2
 * @since Apr 17, 2015
 *
 */
@Table 
public class Def {
	
	public static final String SYSTEM_USER = "sys_user";

	@PrimaryKey	
	private DefPrimaryKey pkey;
	
	@Column(value = "wordkey")
	private String wordKey;
	
	@Column(value = "example")
	private String example;
	
	@Column(value = "curr_review_interval")
	private String currReviewIntv;
	
	@Column(value = "next_review_time")
	private String nextReviewTime;
	
	@Column(value = "add_date")
	private String addDate;
	
	public Def() {}
	
	public Def(String username, String word, String def) {
		this.pkey = new DefPrimaryKey(word, username, def);
	}	
	
	public DefPrimaryKey getPkey() {
		return pkey;
	}

	public void setPkey(DefPrimaryKey pkey) {
		this.pkey = pkey;
	}

	public String getUsername() {
		return pkey.getUsername();
	}

	public void setUsername(String username) {
		pkey.setUsername(username);
	}

	public String getDef() {
		return pkey.getDef();
	}

	public void setDef(String def) {
		pkey.setDef(def);
	}

	public String getWordKey() {
		return wordKey;
	}

	public void setWordKey(String wordKey) {
		this.wordKey = wordKey;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getCurrReviewIntv() {
		return currReviewIntv;
	}

	public void setCurrReviewIntv(String currReviewIntv) {
		this.currReviewIntv = currReviewIntv;
	}

	public String getNextReviewTime() {
		return nextReviewTime;
	}

	public void setNextReviewTime(String nextReviewTime) {
		this.nextReviewTime = nextReviewTime;
	}

	public String getAddDate() {
		return addDate;
	}

	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}

	public static Def getSysUserDef(String word, String def) {
		DefPrimaryKey pk = new DefPrimaryKey(word, SYSTEM_USER, def);
		Def newDef = new Def();
		newDef.setPkey(pk);
		
		return newDef;
	}
	
	@Override
	public String toString() {
		return String.format("%s: %s", pkey.getWord(), pkey.getDef());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
	    if (obj == null)
	    	return false;
	    if (getClass() != obj.getClass())
	    	return false;
	    
	    Def def = (Def)obj;
	    return (this.getPkey().equals(def.getPkey()) &&
	    		this.getExample().equals(def.getExample()) &&
	    		this.getCurrReviewIntv().equals(def.getCurrReviewIntv()) &&
	    		this.getNextReviewTime().equals(def.getNextReviewTime()) &&
	    		this.getAddDate().equals(def.getAddDate()));
	}
	
	@PrimaryKeyClass
	public static class DefPrimaryKey implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8375806412629029271L;

		@PrimaryKeyColumn(name = "word", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
		private String word;

		@PrimaryKeyColumn(name = "username", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
		private String username;
		
		@PrimaryKeyColumn(name = "def", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
		private String def;
		
		public DefPrimaryKey() {}
		
		public DefPrimaryKey(String word, String username, String def) {
			this.word = word;
			this.username = username;
			this.def = def;
		}

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}
		
		public String getDef() {
			return def;
		}

		public void setDef(String def) {
			this.def = def;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
		    int result = 1;
		    result = result * prime + (StringUtils.isEmpty(word) ? 0 : word.hashCode());
		    result = result * prime + (StringUtils.isEmpty(username) ? 0 : username.hashCode());
		    result = result * prime + (StringUtils.isEmpty(def) ? 0 : def.hashCode());
		    return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
		    if (obj == null)
		    	return false;
		    if (getClass() != obj.getClass())
		    	return false;
		    
		    DefPrimaryKey pk = (DefPrimaryKey)obj;
		    if(StringsUtils.isAnyEmpty(this.word, this.username, this.def, pk.word, pk.username, pk.def)) {
		    	return false;
		    }
		    
		    return (this.word.equals(pk.word) && this.username.equals(pk.username) && this.def.equals(pk.def));		   
		}
	}
}
