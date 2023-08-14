import React from 'react';
import { useState, useRef } from 'react';
import MeterBrowser from '../meter/MeterBrowser.jsx';
import Button from '@mui/material/Button'
import TextField from '@mui/material/TextField'
import Box from '@mui/material/Box'

import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { dark } from 'react-syntax-highlighter/dist/esm/styles/prism';

export default function ExpressionBuilder({ meterNames, meters }) {
    const [currentMeter, setCurrentMeter] = useState(null);
    const [currentTag, setCurrentTag] = useState(null);
    const [currentTagValue, setCurrentTagValue] = useState(null);
    const expressionRef = useRef("");

    function handleMaterChanged(option) {
        if (option && option.value) {
            setCurrentMeter(meters[option.value]); 
        } else {
            setCurrentMeter(null);
            setCurrentTag(null);
            setCurrentTagValue(null)
        }
    }

    function handleTagChanged(option) {
        if (option && option.value) {
          setCurrentTag(option.value)
        } else {
          setCurrentTag(null)
          setCurrentTagValue(null)
        }
      }

      function hanleTagValueChanged(option) {
        if (option && option.value) {
            setCurrentTagValue(option.value)
        } else {
            setCurrentTagValue(null)
        }
    }

    function handleAddToExpression() {
       if (currentMeter) {
           addMeterToExpression();
        }
    }

    function addMeterToExpression() {
        document.getElementById("expression-input").value=expressionRef.current + currentMeter.name;
        //alert(expressionRef.current);
        /*
        if (!expressionRef.current.endsWith(currentMeter.name)) {
            
            document.getElementById("expression-input").value=expressionRef.current;
        } else if (currentTag && !expressionRef.current.endsWith("{" + currentTag+ "=")) {
            expressionRef.current = expressionRef.current + "{" + currentTag+ "=";
        }
        expressionRef.current = expressionRef.current + currentMeter.name;
        */
    }

    function expressionChanged(event) {
         if (event && event.target && event.target.value) {
            expressionRef.current = event.target.value;
         }
    }

    return(
        <>
        <MeterBrowser 
            meterNames={meterNames} 
            meters={meters} 
            currentMeter={currentMeter} 
            currentTag={currentTag} 
            currentTagValue={currentTagValue} 
            onMeterChange={handleMaterChanged} 
            onTagChange={handleTagChanged} 
            onTagValueChange={hanleTagValueChanged}/>
        <Button variant="outlined" onClick={handleAddToExpression}>Add to expression</Button>
        <Box sx={{marginTop: 2}}>
        <TextField  
            label="Expression" 
            id="expression-input"
            fullWidth 
            defaultValue=""
            InputProps={{
                readOnly: false,
            }} 
            onInput={expressionChanged}  
            />    
        </Box>
        </>
  );
}