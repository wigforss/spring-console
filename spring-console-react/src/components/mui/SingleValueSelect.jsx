import React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';

export default function SingleValueSelect({ options, label, onChange }) {
    return (
      <>
      <Autocomplete
        options={options}
        onChange={(event, newValue) => {
          onChange(newValue);
        }}
        renderInput={(params) => (
            <TextField {...params} label={label} />
        )}
        isOptionEqualToValue={(option, value) => option.value === value.value}
      />
      </>
    );
  }