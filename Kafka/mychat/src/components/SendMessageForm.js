import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import axios from 'axios';

const SendMessageForm = ({ topic }) => {
  const [username, setUsername] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();

    const body = {
        topic: topic,        
        username: username,  
        message: message     
    };

    try {
      // Enviar el tópico en el body de la petición
      await axios.post('/api/send-message', body);
      setMessage('');  // Limpiar el campo de mensaje después de enviar
    } catch (error) {
      console.error('Error al enviar el mensaje', error);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <TextField
        label="Nombre de usuario"
        variant="outlined"
        fullWidth
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
        style={{ marginBottom: '10px' }}
      />
      <TextField
        label="Mensaje"
        variant="outlined"
        fullWidth
        value={message}
        onChange={(e) => setMessage(e.target.value)}
        required
        style={{ marginBottom: '10px' }}
      />
      <Button variant="contained" color="primary" type="submit" style={{ marginTop: '10px' }}>
        Enviar Mensaje
      </Button>
    </form>
  );
};

export default SendMessageForm;
