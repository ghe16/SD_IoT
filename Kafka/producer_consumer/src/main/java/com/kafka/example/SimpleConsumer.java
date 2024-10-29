package kafka.example1;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProducer {

	Properties props; // The producer is a Kafka client that publishes records to the Kafka cluster.

	KafkaProducer<String, String> producer;
	
//****************************************************************
// 1.KAFKA PRODUCER CONFIGURATION 
//****************************************************************
	SimpleProducer() {
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		// Serializer for conversion the key type to bytes
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		// Serializer for conversion the value type to bytes
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");

		producer = new KafkaProducer<String, String>(props);
	}
	
//****************************************************************
//2. TOPIC PRODUCTION
//****************************************************************	
	void produceAndPrint() {
		for (int i = 0; i < 200; i++)
			// Fire-and-forget send(topic, key, value)
			// Send adds records to unsent records buffer and return
			producer.send(new ProducerRecord<String, String>("DS", Integer
					.toString(i), Integer.toString(i)));

	}

	void stop() {
		producer.close();
	}

	public static void main(String[] args) {
		//System.setProperty("kafka.logs.dir", "/home/isabel/kafka/logs");
		SimpleProducer myProducer = new SimpleProducer();
		myProducer.produceAndPrint();
		myProducer.stop();

	}
}
