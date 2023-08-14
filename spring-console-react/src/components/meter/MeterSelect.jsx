import React from 'react';
import { useMemo } from 'react';
import SingleValueSelect from '../mui/SingleValueSelect.jsx'


export default function MeterSelect({meterNames, meters, onChange}) {
    let meterOptions = [];
    if (meterNames) {
        meterOptions = useMemo(() => {
            return meterNames.map((meterName) => { 
                let optionLabel = meterName;
                if (meters[meterName]) {
                    optionLabel = meterName + " [" +  meters[meterName]["type"] + "]";
                }
                return {value: meterName, label: optionLabel }});
        }, [meterNames]);
    }
      return(
        <SingleValueSelect options={meterOptions} label="Meter" onChange={onChange}/>
   );

    
    
}

