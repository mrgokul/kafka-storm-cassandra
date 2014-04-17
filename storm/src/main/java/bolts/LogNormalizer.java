package bolts;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class LogNormalizer extends BaseBasicBolt {

	public void cleanup() {}

	/**
	 * The bolt will receive the line from the Kafka Spout
	 * 
	 * It will extract the Log Level and floor the time to seconds
	 * "ERROR: 2014-04-16 04:36:29,879 This is an error message" returns
	 * ("ERROR","2014-04-16 04:36:29")
	 */
	public void execute(Tuple input, BasicOutputCollector collector) {
        String sentence = input.getString(0);
        String[] one = sentence.split(",",2);
        String[] words = one[0].split(":",2);       
        String word = words[0] + " " + words[1];
        collector.emit(new Values(words[0],words[1]));
       } 	

	/**
	 * The bolt will emit the fields "word" and "time"
	 */
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word","time"));
	}
}
