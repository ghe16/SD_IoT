import React, { useState, useEffect } from 'react';
import TopicList from './components/TopicList';
import MessageBox from './components/MessageBox';
import SendMessageForm from './components/SendMessageForm';
import CreateTopicForm from './components/CreateTopicForm';
import axios from 'axios';

const App = () => {
  const [topics, setTopics] = useState([]);  // Lista de tópicos
  const [selectedTopic, setSelectedTopic] = useState('');  // Tópico seleccionado
  const [messages, setMessages] = useState([]);  // Mensajes del tópico seleccionado
  const [isPolling, setIsPolling] = useState(false); // Estado para controlar el polling

  // Función para obtener los tópicos desde el backend
  const fetchTopics = async () => {
    try {
      const response = await axios.get('/api/topics');
      setTopics(response.data);
    } catch (error) {
      console.error('Error al obtener los tópicos', error);
    }
  };

  // Cambiar el tópico cuando se selecciona uno nuevo
  const changeTopic = async (topic) => {
    setSelectedTopic(topic);  // Actualizar el tópico seleccionado
    setMessages([]);  // Limpiar los mensajes anteriores
    setIsPolling(true);  // Iniciar el polling cuando se selecciona un tópico

    try {
      await axios.post('/api/change-topic/${topic}');
    } catch (error) {
      console.error('Error al cambiar el tópico', error);
    }
  };

  // Obtener los mensajes del tópico actual
  const fetchMessages = async () => {
    try {
      const response = await axios.get('/api/messages');
      if (response.data.length > 0) {
        setMessages(response.data);  // Actualizar el estado con los mensajes
        setIsPolling(false);  // Detener el polling cuando se reciban mensajes
      }
    } catch (error) {
      console.error('Error al obtener los mensajes', error);
    }
  };

  useEffect(() => {
    fetchTopics();  // Obtener la lista de tópicos al cargar la página
  }, []);

  // useEffect para hacer polling de los mensajes mientras no se reciban
  useEffect(() => {
    if (isPolling && selectedTopic) {
      const interval = setInterval(() => {
        fetchMessages();  // Consultar los mensajes cada 2 segundos
      }, 2000);

      return () => clearInterval(interval);  // Limpiar el intervalo cuando ya no sea necesario
    }
  }, [isPolling, selectedTopic]);

  return (
    <div style={{ padding: '20px' }}>
      <h1>Aplicación de Chat con Kafka</h1>

      {/* Formulario para crear un nuevo tópico */}
      <CreateTopicForm onTopicCreated={fetchTopics} />

      {/* Desplegable para seleccionar un tópico */}
      <TopicList topics={topics} onSelectTopic={changeTopic} />

      {/* Mostrar mensajes del tópico seleccionado */}
      {selectedTopic && (
        <>
          <h2>Mensajes en el tópico: {selectedTopic}</h2>
          <div style={{ marginBottom: '30px' }}>
            <MessageBox messages={messages} />
          </div>

          <h3>Enviar mensaje</h3>
          <SendMessageForm topic={selectedTopic} />
        </>
      )}
    </div>
  );
};

export default App;
