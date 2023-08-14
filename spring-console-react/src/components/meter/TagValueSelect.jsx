import React from "react";
import { useRequestProcessor } from '../../hooks/useRequestProcessor.js';
import { PointSpreadLoading } from 'react-loadingg';
import SingleValueSelect from '../mui/SingleValueSelect.jsx'


export default function TagValueSelect({meter, tagName, onChange}) {
    const { useGet } = useRequestProcessor();

    console.log("Values for tag '" + tagName + "'")
    const { data: tagValues, isLoading, error } = useGet("/meters/" + meter.name + "/tags/" + tagName + "/values", meter.name + "/tags/" + tagName + "/values");
        
    if (isLoading) {
        const loadingStyle = {
            margin: 'auto',
            position: 'relative',
            marginTop: -30,
            marginLeft: 0,
            left: 0,
            top: 0,
            };
        return <PointSpreadLoading size="{small}" style={loadingStyle}/>;
    } 
    if (error){
        return <p>Error: {error.message}</p>;
    }
    if (tagValues) {
        const tagValueOptions = tagValues.map((tagValue) => { 
            return {value: tagValue, label: tagValue }});
        
        return (
            <SingleValueSelect options={tagValueOptions} label="Tag Value" onChange={onChange}/>
        );
    } else {
        return (<SingleValueSelect options={[]} label="Tag Value" onChange={onChange}/>);
    }   
    
}