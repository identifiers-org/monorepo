import { Config } from '../config/config';
import { fetchAndAdd } from '../utils/fetchAndAdd';

import { setPrefixRegistrationSessionListParams } from './PrefixRegistrationSessionListParams';


//
// PrefixRegistrationSessionList actions.
//

// Get Prefix Registration Requests from registry. Will dispatch setPrefixRegistrationSessionList.
export const getPrefixRegistrationSessionListFromRegistry = (params) => {
  return async (dispatch) => {
    const requestUrl = new URL(`${Config.registryApi}/restApi/prefixRegistrationSessions/search/findByClosedFalse`);

    // Comment previous line and use this one to see all requests, even closed ones.
    // const requestUrl = new URL(`${Config.registryApi}/restApi/prefixRegistrationSessions/`);

    Object.keys(params).forEach(param => {
      requestUrl.searchParams.append(param, params[param]);
    });

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();
      await dispatch(setPrefixRegistrationSessionListParams(data.page));
      const prefixRegistrationRequests = await Promise.all(data._embedded.prefixRegistrationSessions.map(pfs => {
        let { _links, ...newResource } = pfs;

        // Store id for curation requests.
        newResource['id'] = _links.self.href.split('/').pop();

        return fetchAndAdd(newResource, [
          {name: 'prefixRegistrationRequest', url: _links.prefixRegistrationRequest.href}
        ]);
      }));

      dispatch(setPrefixRegistrationSessionList(prefixRegistrationRequests));
    }
    catch (err) {
      console.log('Error fetching prefix registration request list: ', err);
    };
  };
};


// Redux store update for prefix registration session list.
export const setPrefixRegistrationSessionList = (prefixRegistrationSessionList) => {
  return {
    type: 'SET_PREFIXREGISTRATIONSESSIONLIST',
    prefixRegistrationSessionList
  };
};
