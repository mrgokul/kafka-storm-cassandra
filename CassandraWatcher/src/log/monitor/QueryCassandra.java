package log.monitor;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import log.monitor.util.DatastaxUtil;


import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class QueryCassandra {

    DatastaxUtil dsu;
    String table;


    public QueryCassandra(String instance, String keyspace, String table){
        this.dsu = new DatastaxUtil(instance, keyspace);
        this.table = table;
     }
    
    private int getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return (int) (TimeUnit.SECONDS.convert(diffInMillies,TimeUnit.MILLISECONDS) );
    }
    
    /**
    * Get latest 30 sec of data from Cassandra. Negative timediffs upto 3s 
    * observed, but ignored.

    **/	

    public Map<String, List<Integer>> get() {

    List<Integer> warningList = new ArrayList<Integer>(Collections.nCopies(30, 0));
    List<Integer> errorList = new ArrayList<Integer>(Collections.nCopies(30, 0));
    List<Integer> criticalList = new ArrayList<Integer>(Collections.nCopies(30, 0));
    Map<String, List<Integer>> logMap = new HashMap<String, List<Integer>>();
    logMap.put("WARNING", warningList);
    logMap.put("ERROR", errorList);
    logMap.put("CRITICAL", criticalList);
    long time = System.currentTimeMillis() - 30000; 
    Date now = new Date();
    String query = "SELECT * from " + table + " WHERE time > " + time + " ALLOW FILTERING;";
    ResultSet out = dsu.querySchema(query);
    for (Row row : out) {
        int timediff = getDateDiff(row.getDate("time"),now);
        if(timediff >= 0){
        logMap.get(row.getString("log_level")).set(timediff,row.getInt("count"));
        }
    }
    return logMap;
}

}
