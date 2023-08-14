import React from "react";
import { useRequestProcessor } from '../../hooks/useRequestProcessor.js';
import { PointSpreadLoading } from 'react-loadingg';
import SingleValueSelect from '../mui/SingleValueSelect.jsx'


export default function TagSelect({meter, onChange}) {
    console.log("meter = " + meter.name);
    const { useGet } = useRequestProcessor();

    console.log("Loading tags")

    const { data: tags, isLoading, error } = useGet("/meters/" + meter.name + "/tags", meter.name,  meter.name + "/tags", meter.name);
        
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
    if (tags) {
        //return <p>Tags: {tags}</p>;
        const tagOptions = tags.map((tagName) => { 
            return {value: tagName, label: tagName }})
        return (
            <SingleValueSelect options={tagOptions} label="Tag" onChange={onChange}/>
        );
    }    
    return "";
}