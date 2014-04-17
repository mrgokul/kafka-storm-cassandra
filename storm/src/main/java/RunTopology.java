import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.spout.SchemeAsMultiScheme;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;

import bolts.LogCountCassandraBolt;
import bolts.LogNormalizer;


public class RunTopology {
        
	public static void main(String[] args) throws InterruptedException {
       
	    //Set up Kafka Spout
        SpoutConfig kafkaConf = new SpoutConfig(new SpoutConfig.ZkHosts("localhost","/brokers"),
									            "test", 
												"/kafkastorm",
												"discovery");
        kafkaConf.scheme = new SchemeAsMultiScheme(new StringScheme());

        KafkaSpout kafkaSpout = new KafkaSpout(kafkaConf);
		
        //Topology definition
                TopologyBuilder builder = new TopologyBuilder();
                builder.setSpout("kafka-spout",kafkaSpout);
                builder.setBolt("log-normalizer", new LogNormalizer())
                        .shuffleGrouping("kafka-spout");
                builder.setBolt("log-counter", new LogCountCassandraBolt(),1)
                        .fieldsGrouping("log-normalizer", new Fields("time"));

        //Configuration
                Config conf = new Config();
                conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
				
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("Kafka-Storm-Cassandra", conf, builder.createTopology());
                Thread.sleep(200000);
                cluster.shutdown();
        }
}

