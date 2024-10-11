import React from 'react';
import { List, ListItem, ListItemText, Paper } from '@mui/material';

const MessageBox = ({ messages }) => {
  return (
    <Paper style={{ padding: '10px', marginTop: '20px', maxHeight: '300px', overflowY: 'auto' }}>
      <List>
        {messages.length > 0 ? (
          messages.map((msg, index) => {
            const parsedMessage = JSON.parse(msg);  // Si los mensajes son JSON, parsearlos
            const username = parsedMessage.username || "Anónimo";  // Asegurarse de que el nombre de usuario existe
            const messageText = parsedMessage.message || "Mensaje vacío";  // Asegurarse de que el mensaje existe
            return (
              <ListItem key={index}>
                <ListItemText primary={`${username}: ${messageText}`} />
              </ListItem>
            );
          })
        ) : (
          <ListItem>
            <ListItemText primary="No hay mensajes en este tópico" />
          </ListItem>
        )}
      </List>
    </Paper>
  );
};

export default MessageBox;
