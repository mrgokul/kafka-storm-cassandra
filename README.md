kafka-storm-cassandra
=====================

Real time processing with a simple example

---------------------

###Kafka
This setup worked for me:

* Kafka 0.7.2
* sbt 2.9.2
* jdk-6

###Cassandra
Install Cassandra 2+. Create a keyspace and a table with the following commads.
```sql
CREATE KEYSPACE test WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
use test;
CREATE TABLE logs(time timestamp, log_level varchar, count int, primary key(log_level, time));
```

###Storm
Run the Storm Topology.
```
mvn -e clean compile exec:java -Dexec.mainClass=RunTopology
```

####Send to Kafka
Tail a continuosly growing log file to Kafka. 
```
python log.py &
python log2kafka.py -l example.log -t test
```

###Visualize

A simple websocket application that continously queries Cassandra on the server side, and renders a time series on the client side.

1. Get [https://github.com/shutterstock/rickshaw](rickshaw) and add it to the WebContent folder
2. Get Glassfish 4
3. Add the dependecies of cassandra driver to ext folder inside Glassfish
4. Start the server

If all goes well, see below:

