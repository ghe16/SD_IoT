package com.kafka.example;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

public class SimpleConsumer {

    public static void main(String[] args) {    
        // a client that consumes records from the kafka cluster using TCP connections.
        // KAfka service transparently handles the failure of kafka brokers and partition topic migration

        //********************************************************
        // 1 KAFKA Consumer client configuration
        //************************************************

        Properties consumerConfig;
        KafkaConsumer < String, String > consumer;

        // Configuration of the Kafka consumer properties

        consumerConfig = new Properties();
        consumerConfig.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // First, a know group to keep track of the records in the kafka cluster 
        //consumerConfig.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "GRUPO1");
        
        //But, we need to use a random group for consuming the records in the diverse executions
        consumerConfig.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        consumerConfig.setProperty(ConsumerConfig.AUTO_OFFSET_CONFIG,"earliest");

        //if the information is coming serilized, then we need to undo it
        consumerConfig.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumerConfig.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // props.setProperty("auto.commit.interval.ms","1000");

        //Configuration of the System's properties
        System.getProperty("kafka.logs.dir", "/tmp/kafka-logs");
        System.getProperties().list(System.out);
        
        //********************************************************
        // 2 KAFKA Consumer client creation and topic subscriptions
        //************************************************

        consumer = new KafkaConsumer<String,String>(consumerConfig);
        consumer.subscribe(Arrays.asList("DS"));

        //********************************************************
        // 3 Topic consumption
        //************************************************

        System.out.prinln("Starting the kafka client %n");
        // loop forever to be consuming messages from the cluster
        while(true) {
            ConsumerRecords<String,String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String,String> record : records) {
                System.out.println("Something consumed");
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
        consumer.close();
    
    }
}

