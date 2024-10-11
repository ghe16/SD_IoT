package com.example.chat_backend.controller.service;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Properties;
import java.util.UUID;

@Service
public class KafkaMessageService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private KafkaConsumer<String, String> consumer;
    private Thread consumerThread;
    private final Queue<String> messageQueue = new LinkedList<>();  // Cola de mensajes para aislar KafkaConsumer

    public KafkaMessageService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Método para actualizar el consumidor cuando cambie el tópico
    public synchronized void updateConsumer(String topic) {
        stopConsumer();  // Detener el consumidor anterior antes de iniciar uno nuevo

        // Configurar las propiedades del nuevo consumidor
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "138.100.154.74:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "dynamic-consumer-group-" + UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Crear el nuevo consumidor
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));  // Suscribirse al nuevo tópico

        // Iniciar un nuevo hilo para consumir mensajes
        consumerThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {  // Verificar si el hilo ha sido interrumpido
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    synchronized (messageQueue) {  // Proteger el acceso a la cola de mensajes
                        records.forEach(record -> {
                            messageQueue.add(record.value());  // Almacenar los mensajes recibidos en la cola
                            System.out.println("Mensaje recibido: " + record.value());
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                consumer.close();  // Cerrar el consumidor cuando el hilo es interrumpido
            }
        });
        consumerThread.start();
    }

    // Método para detener el consumidor y el hilo anterior
    public synchronized void stopConsumer() {
        if (consumerThread != null && consumerThread.isAlive()) {
            consumerThread.interrupt();  // Interrumpir el hilo del consumidor
            try {
                consumerThread.join();  // Esperar a que el hilo termine completamente
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (consumer != null) {
            consumer.close();  // Cerrar el consumidor Kafka
            consumer = null;  // Liberar el recurso
        }
    }

    // Método para obtener todos los mensajes recibidos
    public synchronized List<String> getAllMessages() {
        synchronized (messageQueue) {  // Sincronizar el acceso a la cola de mensajes
            return new LinkedList<>(messageQueue);  // Devolver una copia de la cola de mensajes
        }
    }

    // Método para limpiar la lista de mensajes cuando se cambia el tópico
    public synchronized void clearMessages() {
        synchronized (messageQueue) {  // Sincronizar el acceso a la cola de mensajes
            messageQueue.clear();
        }
    }



    //Send a message to a kafka topic
    public void sendMessage(String topic,String username, String message){
        String keyValueMessage =  "{\"username\":\"" + username + "\", \"message\":\"" + message + "\"}";  
        kafkaTemplate.send(topic, keyValueMessage);
    }


}
