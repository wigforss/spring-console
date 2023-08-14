import React from 'react';
import {useEffect, useState } from 'react';
import { QueryClient, QueryClientProvider } from "react-query";
import config from "./config.json";
import ExpressionBuilder from './components/expression/ExpressionBuilder.jsx';
import DynamicIcon from './components/mui/DynamicIcon.jsx'


const queryClient = new QueryClient({});


const metricSubscribers = {}

function subscribeToMetric(meterName) {
  console.log("Subscribe to " + meterName)
  if (!(meterName in metricSubscribers)) {
    console.log("Subscribing");
    const evtSource = new EventSource(config.serverURL + "/meters/" + meterName + "/value/events");
    evtSource.onmessage = (event) => {
      const meterValues = JSON.parse(event.data);
      console.log(meterValues.data[""]);
    };
    metricSubscribers[meterName]=evtSource;
    console.log(metricSubscribers);
  }
  console.log("Done")
}





export default function App() {
  const [meters, setMeters] = useState([]);
  const [meterNames, setMeterNames] = useState([]);

  

  useEffect(() => {
    fetch(config.serverURL + "/meters/names")
    .then((response) => response.json())
    .then((data) => {
       setMeterNames(data);
    })
    .catch((err) => {
       console.error(err.message);
    });
    fetch(config.serverURL + "/meters")
       .then((response) => response.json())
       .then((data) => {
          setMeters(data);
       })
       .catch((err) => {
          console.log(err.message);
       });
      
 }, []);


  return (
    <QueryClientProvider client={queryClient}>
      <>
        <h1>App</h1>
        <ExpressionBuilder meterNames={meterNames} meters={meters}/>
      </>
    </QueryClientProvider>
  );
}