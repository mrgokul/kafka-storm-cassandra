kafka-storm-cassandra
=====================

Real time processing with a simple example

---------------------

###Kafka
The setup that worked for me:

* Kafka 0.7.2
* sbt 2.9.2
* jdk-6

###Cassandra
Install Cassandra 2.0.x. Create a keyspace and a table with the following commads.
```sql
CREATE KEYSPACE test WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};
use test;
create table logs(time timestamp, log_level varchar, count int, primary key(log_level, time));
```


