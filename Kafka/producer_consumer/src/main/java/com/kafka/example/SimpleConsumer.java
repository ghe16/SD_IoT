package kafka.example1;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;


public class SimpleConsumer {
	// http://syncor.blogspot.com.es/2013/09/getting-started-with-log4j-2-in-eclipse.html

	public static void main(String[] args) {

		// A client that consumes records from a Kafka cluster using TCP
		// connections. Kafka service transparently handles the failure of Kafka brokers and
		// partition topic migration
		
//****************************************************************
// 1. KAFKA CONSUMER CLIENT CONFIGURATION 
//****************************************************************

		Properties consumerConfig;
		KafkaConsumer<String, String> consumer;

		//Configuration of the properties of the Kafka consumer
		consumerConfig = new Properties();
		consumerConfig.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				"localhost:9092");
		// consumerConfig
		// .setProperty(ConsumerConfig.GROUP_ID_CONFIG, "Grupo1");
		
		// We need to use a random group for consuming the same records in different executions
		consumerConfig.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID
				.randomUUID().toString());
		consumerConfig.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
				"earliest");
		consumerConfig.setProperty(
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		consumerConfig.setProperty(
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringDeserializer");
		// props.setProperty("auto.commit.interval.ms", "1000");
		   
		//Configuration of the properties of the system
		System.setProperty("kafka.logs.dir", "/tmp/kafka-logs");
		System.getProperties().list(System.out);
		
//****************************************************************
//  2. KAFKA CONSUMER CLIENT CREATION AND TOPIC SUBSCRIPTION
//****************************************************************
		consumer = new KafkaConsumer<String, String>(consumerConfig);
		consumer.subscribe(Arrays.asList("DS"));
		
//****************************************************************
//  3. TOPIC CONSUMPTION
//****************************************************************		
		System.out.printf("Starting the Kafka client %n");
		
		//Start forever consuming records 
		boolean end = false;
		while ( !end) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
		
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("something consumed");
				System.out.printf("offset = %d, key = %s, value = %s%n",
						record.offset(), record.key(), record.value());
			}
		}
		consumer.close();
	}
}
