
import React from 'react';
import { useState } from 'react';
import TagSelect from './TagSelect.jsx';
import TagValueSelect from './TagValueSelect.jsx';
import usePrevious from '../../hooks/usePrevious.js';
import Box from '@mui/material/Box'

export default function TagBrowser({ meter, currentTag, currentTagValue, onTagChange, onTagValueChange }) {
    const { useValueChanged } = usePrevious();
    

    function renderTagValueSelect() {
        return meter && currentTag && !hasMeterChanged;
    }

    const hasMeterChanged = useValueChanged(meter, (cv, pv) => cv.name === pv.name);
    console.log("Has meter changed: " + hasMeterChanged);
  
    if (renderTagValueSelect()) {
        return (
            <>
            <Box sx={{marginBottom: 2}}>
                <TagSelect meter={meter} onChange={onTagChange} />
            </Box>
            <TagValueSelect meter={meter} tagName={currentTag} onChange={onTagValueChange} />
            
            </>
        );
    } else if (meter) {
        return (
            <>
            <Box sx={{marginBottom: 2}}>
                <TagSelect meter={meter} onChange={onTagChange} />
            </Box>
            </>
        );
    }
    /*
    if (typeof meter !== "undefined" && meter) { 
        console.log("Render TagBrowser meter = '" + meter.name + "'");
        if (renderTagValueSelect()) {
            return (
                <>
                <Box sx={{marginBottom: 2}}>
                    <TagSelect meter={meter} onChange={onTagChange} />
                </Box>
                <TagValueSelect meter={meter} tagName={currentTag} onChange={onTagValueChange} />
                
                </>
            );
        } else if (meter) {
            return (
                <>
                <div>
                <TagSelect meter={meter} onChange={onTagChange} />
                </div>
                </>
            );
        } else {
            return (
               ""
            );
        }
    }
    return "";
    */
}