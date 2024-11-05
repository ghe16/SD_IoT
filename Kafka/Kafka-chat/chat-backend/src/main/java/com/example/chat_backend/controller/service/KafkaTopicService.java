package com.example.chat_backend.controller.service;


import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaTopicService {

    private final String bootstrapServer = "localhost:9092";


    //methdo to obtain all kafaka topics
    public Set<String> getAllTopics() throws ExecutionException, InterruptedException{
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        try (AdminClient client = AdminClient.create(props)) {
            return client.listTopics().names().get();
        }
    }

    //method to create a new kafka topic

    // public void createTopic(String topicName, int partitions, short replicationFactor)  {
    public void createTopic(String topicName){
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        try(AdminClient client = AdminClient.create(props))  {
            //NewTopic newTopic = new NewTopic(topicName, partitions, replicationFactor);
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            Set<NewTopic> newTopics = Collections.singleton(newTopic);
            client.createTopics(newTopics);
        }

    }
} 
