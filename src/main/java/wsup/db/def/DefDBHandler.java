package wsup.db.def;

import java.util.List;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import wsup.db.CassandraDaoOperator;

public class DefDBHandler extends CassandraDaoOperator {

	public List<Def> select(String word) {
		Select selStmt = QueryBuilder.select().from("def");
		selStmt.where(QueryBuilder.eq("word", word));
		selStmt.allowFiltering();
		
		return getCassandraOps().select(selStmt, Def.class);
	}
	
	public List<Def> selectAll(String username) {
		Select selStmt = QueryBuilder.select().from("def");
		selStmt.where(QueryBuilder.eq("username", username));
		
		return getCassandraOps().select(selStmt, Def.class);
	}
	
	public Def insert(Def newDef) {
		return getCassandraOps().insert(newDef);
	}
	
	public List<Def> insertAll(List<Def> defList) {
		return getCassandraOps().insert(defList);
	}
	
	public Def update(Def def) {
		return getCassandraOps().update(def);
	}
	
	public List<Def> updateAll(List<Def> defList) {
		return getCassandraOps().update(defList);
	}
	
	public void delete(Def def) {
		getCassandraOps().delete(def);
	}
	
	public void deleteAll(List<Def> defList) {
		getCassandraOps().delete(defList);
	}
}
