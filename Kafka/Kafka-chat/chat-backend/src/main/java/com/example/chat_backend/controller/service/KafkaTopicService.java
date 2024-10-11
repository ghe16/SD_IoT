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

    private final String bootstrapServer = "138.100.154.74:9092";


    //methdo to obtain all kafaka topics
    public Set<String> getAllTopics() throws ExecutionException, InterruptedException{
        //TODO: 
        // Create properties, put the setup and listTopics
        
    }

    //method to create a new kafka topic
    public void createTopic(String topicName){
        // Create properties, put the setup and create NewTopic
        
    }
} 