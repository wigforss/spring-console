import { useQuery } from 'react-query';
import axiosClient from "axios";
import config from "../config.json";

export function useRequestProcessor() {
  //const queryClient = useQueryClient();


  /*
  function mutate(key, mutationFunction, options = {}) {
    return useMutation({
      mutationKey: key,
      mutationFn: mutationFunction,
      onSettled: () => queryClient.invalidateQueries(key),
      ...options,
    });
  }
*/
  function useGet(path, cacheKey, options = { enabled: true }) {

    return useQuery(
      cacheKey,
      (() => axiosClient.get(config.serverURL +  path).then((res) => res.data)), options);

  }

  return { useGet };
}