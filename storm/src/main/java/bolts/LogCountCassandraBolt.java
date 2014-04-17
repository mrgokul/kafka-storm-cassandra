package bolts;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import util.DatastaxUtil;

public class LogCountCassandraBolt extends BaseBasicBolt {

        Map<String, Integer> counters;
        String currentTime;
        boolean first;
        DatastaxUtil dsu;

        /**
         * When the cluster shuts down successfully
         * 
         */
        @Override
        public void cleanup() {
                System.out.println("-- Word Counter Loaded to Cassandra ==");
                dsu.close();
                }


        /**
         * First method called
         */
        @Override
        public void prepare(Map stormConf, TopologyContext context) {
                this.counters = new HashMap<String, Integer>();
                this.currentTime = "";
                this.first = true;
                this.dsu = new DatastaxUtil("ec2-54-193-65-48.us-west-1.compute.amazonaws.com", "casstest");
        }

        /**
         * Nothing to emit
         */
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {}

        public void resetMap(){
                counters.put("WARNING",0);
                counters.put("ERROR",0);
                counters.put("CRITICAL",0);
        }

		
		 /**
         * Returns a CQL statement 
         */
        public String getStatement(String time, String level, int count){

                String query  = " INSERT INTO logs (time, log_level, count)" +
                                                    " values ('" + time + "','" + level +
                                                     "',"+ count +");";
               
                return query;
        }

		/** A hacky but working code for a non-parallel setup. The aim here 
		* is to demonstrate a simple use of the kafka-storm-cassandra architecture.
		* Do see the RollingTopWords example from storm-starter project to 
		* see how moving window computations are performed.	
		* e.g. Loads into Cassadra ("ERROR","2014-04-16 04:36:29",3)
		*/		
		
        @Override
        public void execute(Tuple input, BasicOutputCollector collector) {
            String time = input.getString(1);
            String logLevel = input.getString(0);
            if(!time.equals(currentTime)){
                currentTime = time;
                    if(!first){
                        String statementW = getStatement(time, "WARNING", counters.get("WARNING"));
                        String statementE = getStatement(time, "ERROR", counters.get("ERROR"));
                        String statementC = getStatement(time, "CRITICAL", counters.get("CRITICAL"));
                        dsu.loadData(statementW);
                        dsu.loadData(statementE);
                        dsu.loadData(statementC);
                        System.out.println(counters);
                    }
                resetMap();
                first = false;
            }
            Integer c = counters.get(logLevel) + 1;
            counters.put(logLevel,c);
        }
}
