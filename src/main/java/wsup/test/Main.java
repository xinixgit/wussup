package wsup.test;

import java.util.List;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import wsup.db.def.Def;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class Main {
	public static void main(String[] arg) {
		try {
			Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").build(); 
			Session session = cluster.connect("wussup"); 
			CassandraOperations ops = new CassandraTemplate(session); 

			Def newDef = Def.getSysUserDef("justify", "to provide or be a good reason for (something)");
			ops.insert(newDef);
			
			List<Def> results = ops.select("select * from def", Def.class);
			System.out.println(results.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
