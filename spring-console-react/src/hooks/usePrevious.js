import { useEffect, useRef } from 'react';

export default function usePrevious() {
   
    function usePreviousValue(value) {
      const ref = useRef();
      useEffect(() => {
        ref.current = value; //assign the value of ref to the argument
      },[value]); //this code will run when the value of 'value' changes
      return ref.current; //in the end, return the current ref value.
    }

    function useValueChanged(value, equalFn) {
      const previousValue = usePreviousValue(value);
      if (previousValue !== 'undefined' && previousValue && value !== 'undefined' && value) {
        if (equalFn) {
          return !equalFn(value, previousValue);
        } else {
          return value === previousValue
        }
      }
      return true;
    }

    return { usePreviousValue, useValueChanged };
}
  