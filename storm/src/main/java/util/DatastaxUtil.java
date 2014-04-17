package log.monitor.util;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class DatastaxUtil {
        Builder builder;
        Cluster cluster;
        Session session;
 
    public  DatastaxUtil(String node, String keyspace) {
    	cluster = Cluster.builder().addContactPoint(node).build();
    	Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n",
        metadata.getClusterName());
        for ( Host host : metadata.getAllHosts() ) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
                 host.getDatacenter(), host.getAddress(),
            host.getRack());
        }
        session = cluster.connect(keyspace);
    }


    public void loadData(String query) {
        session.executeAsync(query);
        }

    public ResultSet querySchema(String query) {
    	ResultSet results =session.execute(query);
    	return results;
        }

    public void close() {
        session.close();
        if (cluster != null){
            cluster.close();
        }
    }


 }
