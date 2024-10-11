import React from 'react';
import { Select, MenuItem, InputLabel, FormControl } from '@mui/material';

const TopicList = ({ topics, onSelectTopic }) => {
  const handleChange = (event) => {
    onSelectTopic(event.target.value);
  };

  return (
    <FormControl fullWidth>
      <InputLabel id="select-topic-label">Selecciona un tópico</InputLabel>
      <Select labelId="select-topic-label" onChange={handleChange}>
        {topics.map((topic, index) => (
          <MenuItem key={index} value={topic}>
            {topic}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default TopicList;
