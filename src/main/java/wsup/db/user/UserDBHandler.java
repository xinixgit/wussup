package wsup.db.user;

import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import wsup.db.CassandraDaoOperator;

public class UserDBHandler extends CassandraDaoOperator {
	
	public User select(String username) {
		Select selStmt = QueryBuilder.select().from("user");
		selStmt.where(QueryBuilder.eq("username", username));
		
		User user = getCassandraOps().selectOne(selStmt, User.class);
		return user;
	}
	
	public void insert(User user) {
		getCassandraOps().insert(user);
	}
	
	public void update(User user) {
		getCassandraOps().update(user);
	}
	
	public void addFriend(String username, String friendName) {
		Statement stmt = QueryBuilder.update("user")
							.with(QueryBuilder.append("friends", friendName))
							.where(QueryBuilder.eq("username", username));
		
		getCassandraOps().getSession().execute(stmt);
	}
	
	public void deleteFriend(String username, String friendName) {
		Statement stmt = QueryBuilder.update("user")
				.with(QueryBuilder.discard("friends", friendName))
				.where(QueryBuilder.eq("username", username));

		getCassandraOps().getSession().execute(stmt);
	}
}
