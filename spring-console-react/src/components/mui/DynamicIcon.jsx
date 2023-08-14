import React from 'react';
import * as MuiIcons from '@mui/icons-material'
import {
  AccessAlarm, 
  AccessTime,
  Add, 
  AddBoxOutlined,
  AddBoxTwoTone,
  Delete, 
  Equalizer,
  EqualizerTwoTone, 
  Home, 
  MoreTime,
  NetworkCheck,
  PlusOne, 
  Speed,
  TimerOutlined,
  TimerTwoTone} from '@mui/icons-material'


export default function DynamicIcon({ iconNameSupplier, options}) {
    let iconName = iconNameSupplier;
   
    if (typeof(iconName) === 'function') {
      iconName = iconNameSupplier();
    }
  

    switch(iconName) {
      case 'AccessAlarm':
        return <AccessAlarm {...options}/>;
      case 'AccessTime':
        return <AccessTime {...options}/>;
      case 'Add':
        return <Add {...options}/>;
      case 'AddBoxOutlined':
        return <AddBoxOutlined {...options}/>;
      case 'AddBoxTwoTone':
        return <AddBoxTwoTone {...options}/>;
      case 'Delete':
        return <Delete {...options}/>;
      case 'EqualizerTwoTone':
        return <EqualizerTwoTone {...options}/>
      case 'Equalizer':
        return <Equalizer {...options}/>
      case 'Home':
        return <Home {...options}/>;
      case 'MoreTime':
        return <MoreTime {...options}/>
      case 'NetworkCheck':
        return <NetworkCheck {...options}/>
      case 'PlusOne':
        return <PlusOne {...options}/>
      case 'Speed':
        return <Speed {...options}/>
      case 'TimerOutlined':
        return <TimerOutlined {...options}/>
      case 'TimerTwoTone':
        return <TimerTwoTone {...options}/>
      default:
        return "";
    }
    
  
  }