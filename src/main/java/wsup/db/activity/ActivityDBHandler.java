package wsup.db.activity;

import java.util.List;
import java.util.Set;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import wsup.db.CassandraDaoOperator;

/**
 * DBHandler for all activities.
 *
 * @author xdai2
 * @since May 7, 2015
 *
 */
public class ActivityDBHandler extends CassandraDaoOperator {
	public List<Activity> select(Set<String> fromUsers, String type) {
		return select(fromUsers, -1, type);
	}
	
	public List<Activity> select(Set<String> fromUsers, long since, String type) {
		return select("", fromUsers, since, type);
	}
	
	/**
	 * @param targetedUser Username of the targeted user
	 * @param fromUsers A set of users from whom the activities will be selected from
	 * @param since All activities later than this time.
	 * @param type Activity type
	 * @return Activities from a set of users, with a specific username as targeted user.
	 */
	public List<Activity> select(String targetedUser, Set<String> fromUsers, long since, String type) {
		String[] inClause = fromUsers.toArray(new String[0]);
		
		Select selStmt = QueryBuilder.select().from("activity");
		selStmt.allowFiltering();
		selStmt.where(QueryBuilder.in("username", (Object[])inClause))
				.and(QueryBuilder.gt("datetime", since))
				.and(QueryBuilder.eq("targeted_user", targetedUser))
				.and(QueryBuilder.eq("type", type));
		
		return getCassandraOps().select(selStmt, Activity.class);
	}
	
	public Activity insert(Activity acti) {
		return getCassandraOps().insert(acti);
	}
	
	public Activity update(Activity acti) {
		return getCassandraOps().update(acti);
	}
}
