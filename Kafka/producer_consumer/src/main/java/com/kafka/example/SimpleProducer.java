package com.kafka.example;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleProducer {
    Properties props;   //  the producer is a kafka client that publishes messages to the kafka cluster
    public static int numberofMessages = 200;
    KafkaProducer <String, String> producer;

    //********************************************************
    // 1 KAFKA Producer configuration
    //*************************************************

    SimpleProducer(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks","all");
        // Serializer for converting the key type to bytes
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //Serializer for converting the value type to bytes
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer <String, String>(props);
    }

    //********************************************************
    // 2 Topic Production
    //*************************************************

    void prodcueAndPrint(){
        for (int i = 0; i < numberofMessages; i++)
        // Fire and forget send(topic, key, value)
        // Send addss records to unsent records buffer and return
        producer.send(new ProduceRecord<String, String>("DS", Integer.toString(i), Integer.toString(i)));

    }

    void stop() {
        producer.close();
    }


    public static void main(String[] args) {
        SimpleProducer myProducer = new SimpleProducer();
        myProducer.prodcueAndPrint();
        myProducer.stop();
    }

}
