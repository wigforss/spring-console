import React from 'react';

import MeterSelect from './MeterSelect2.jsx';
import TagBrowser from './TagBrowser.jsx';
import TextField from '@mui/material/TextField'
import config from '../../config.json'
import Box from '@mui/material/Box'

export default function MeterBrowser({ meterNames, meters, currentMeter, currentTag, currentTagValue, onMeterChange, onTagChange, onTagValueChange }) {
  
    return (
      <>
        <Box sx={{ border: 1,  borderRadius: 2, boxShadow: 2, margin: 1 , padding: 2, maxWidth: 600 }}>
        <Box sx={{marginBottom: 2}}>
          <Box sx={{marginBottom: 2}}>
            <MeterSelect meterNames={meterNames} meters={meters} onChange={onMeterChange}/>
          </Box>
          <TextField  label="Description" disabled variant="standard" multiline value={currentMeter ? currentMeter["description"] : ""} readOnly={true} fullWidth sx={{"& .MuiInputBase-input.Mui-disabled": {WebkitTextFillColor: "#000000"}}}/>
          <TextField  label="Unit" disabled variant="standard" value={currentMeter ? currentMeter["baseUnit"] : ""} readOnly={true} sx={{"& .MuiInputBase-input.Mui-disabled": {WebkitTextFillColor: "#000000"}}}/>
          <TextField  label="Type" disabled variant="standard" value={currentMeter ? config.meter.type[currentMeter["type"]].label : ""} readOnly={true} sx={{"& .MuiInputBase-input.Mui-disabled": {WebkitTextFillColor: "#000000"}}}/>
        </Box>
        <TagBrowser meter={currentMeter} currentTag={currentTag} currentTagValue={currentTagValue} onTagChange={onTagChange} onTagValueChange={onTagValueChange}/>
        </Box>
        </>
    );
   
  }