package com.example.chat_backend.controller;

import com.example.chat_backend.controller.service.KafkaMessageService;
import com.example.chat_backend.controller.service.KafkaTopicService;

import org.apache.kafka.common.requests.CreateTopicsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;



@RestController
@RequestMapping("/api")
public class KafkaController {


    @Autowired
    private KafkaMessageService kafkaMessageService;
    //private final KafkaMessageService kafkaMessageService;
    private final KafkaTopicService kafkaTopicService;

    public KafkaController(KafkaMessageService kafkaMessageService, KafkaTopicService kafkaTopic){
        this.kafkaMessageService = kafkaMessageService;
        this.kafkaTopicService = kafkaTopic;
    }

    // endpoint to get all topics. 
    @GetMapping("/topics")
    public Set<String> getAllTopics() throws ExecutionException, InterruptedException {
        return kafkaTopicService.getAllTopics();
    }


    // endpoint to create a new topic
    @PostMapping("/create-topic")
    public String createTopic(@RequestBody CreateTopicsRequest request) {
        kafkaTopicService.createTopic(request.getTopicName());
        return "Topiccreated" + request.getTopicName();
    }
    
    // Class for the creation of the topic
    public static class CreateTopicsRequest {
        private String topicName;

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }
    }

    //Endpoint to sned messages to a topic
    @PostMapping("/send-message")    
    public String sendMessage(@RequestBody SendMessageRequest request)  {
        kafkaMessageService.sendMessage(request.getTopic(), request.getUsername(), request.getMessage());
        //public String sendMessage(@RequestParam String topic, @RequestBody SendMessageRequest request)  {
    //    kafkaMessageService.sendMessage(topic, request.getUsername(), request.getMessage());
        return "Message sent to topic: " + request.getTopic();
    }

    //class for the send Medsage Request

    public static class SendMessageRequest {
        private String username;
        private String message;
        private String topic;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic){
            this.topic = topic;
        }
        
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }




    @PostMapping("/change-topic/{topic}")
    public String changeTopic(@PathVariable String topic) {
        kafkaMessageService.clearMessages();  // Limpiar los mensajes anteriores
        kafkaMessageService.updateConsumer(topic);  // Actualizar el consumidor con el nuevo tópico
        return "Tópico cambiado a: " + topic;
    }

    // Endpoint para obtener los mensajes del tópico actual
    @GetMapping("/messages")
    public List<String> getMessages() {
        return kafkaMessageService.getAllMessages();
    }



    // endpoint to obtain all messages from a topic.
    @GetMapping("/messages/{topic}")
    public List<String> getMessages(@PathVariable String topic) {
        return kafkaMessageService.getAllMessages();
    }
    
}
