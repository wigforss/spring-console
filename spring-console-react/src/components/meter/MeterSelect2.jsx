import React from 'react';
import { useMemo, useEffect } from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import Box from '@mui/material/Box';
import DynamicIcon from '../mui/DynamicIcon.jsx'
import config from '../../config.json'


export default function MeterSelect({meterNames, meters, onChange}) {


    let meterOptions = [];

    meterOptions = useMemo(() => {
        return meterNames.map((meterName) => { 
            let optionLabel = meterName;
            const prefix = meterName.split('.', 1)[0];
            if (meters[meterName]) {
                return {value: meterName, label: optionLabel, prefix: prefix, type: meters[meterName]["type"]};
            } else {
                return {value: meterName, label: meterName, prefix: prefix};
            }
        })
    }, 
    [meterNames, meters]);
 

    if (meterNames) {
        
    return (
        <>
        <Autocomplete
        options={meterOptions}
        id="current-meter"
        onChange={(event, newValue) => {
          onChange(newValue);
        }}
        isOptionEqualToValue={(option, value) => option.value === value.value}
        renderInput={(params) => (
          <TextField {...params} label="Meter"/>
        )}
        renderOption={(props, option) => (
        <Box component="li" sx={{ '& > img': { mr: 2, flexShrink: 0 } }} {...props}>
             <DynamicIcon iconNameSupplier={() => config.meter.type[option.type].icon}/> &nbsp; {option.label}
        </Box>
      )}
      groupBy={(option) => option.prefix}
      />
      </>
    );   
    
   }
}
