import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import axios from 'axios';

const CreateTopicForm = ({ onTopicCreated }) => {
  const [newTopic, setNewTopic] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      await axios.post('/api/create-topic', { topicName: newTopic });
      setNewTopic('');  // Limpiar el campo de nuevo tópico
      onTopicCreated();  // Actualizar la lista de tópicos después de crear uno nuevo
    } catch (error) {
      console.error('Error al crear el tópico', error);
    }
  };

  return (
    <form onSubmit={handleSubmit} style = {{marginBottom: '20px'}}>
      <TextField
        label="Nuevo Tópico"
        variant="outlined"
        fullWidth
        value={newTopic}
        onChange={(e) => setNewTopic(e.target.value)}
        required
        style = {{marginBottom: '10px'}}
      />
      <Button variant="contained" color="primary" type="submit">
        Crear Tópico
      </Button>
    </form>
  );
};

export default CreateTopicForm;
