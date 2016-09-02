package wsup.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;

public abstract class CassandraDaoOperator {
	@Autowired
	private CassandraOperations cassandraOps;
	
	public CassandraOperations getCassandraOps() {
		return cassandraOps;
	}

	public void setCassandraOps(CassandraOperations cassandraOps) {
		this.cassandraOps = cassandraOps;
	}
}
