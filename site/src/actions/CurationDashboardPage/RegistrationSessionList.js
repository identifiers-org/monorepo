// Config.
import { config } from '../../config/Config';

// Utils.
import { fetchAndAdd } from '../../utils/fetchAndAdd';
import { renewToken } from '../../utils/auth';

import { setRegistrationSessionListParams } from './RegistrationSessionListParams';


//
// RegistrationSessionList actions.
//

// Get Registration Requests from registry. Will dispatch setRegistrationSessionList.
export const getRegistrationSessionListFromRegistry = (params, registrationSessionType) => {
  return async (dispatch, getState) => {
    const requestUrl = new URL(`${config.registryApi}/restApi/${registrationSessionType}RegistrationSessions/search/findByClosedFalse`);
    const authToken = await renewToken();
    const init = {headers: {'Authorization': `Bearer ${authToken}`}};
    let data;

    Object.keys(params).forEach(param => {
      requestUrl.searchParams.append(param, params[param]);
    });

    try {
      const response = await fetch(requestUrl, init);
      data = await response.json();
    }
    catch (err) {
      console.log(`Error fetching ${registrationSessionType} registration request list: `, err);
      return;
    };

    await dispatch(setRegistrationSessionListParams(data.page, registrationSessionType));
    const registrationRequests = await Promise.all(data._embedded[`${registrationSessionType}RegistrationSessions`].map(pfs => {
      let { _links, ...newResource } = pfs;

      // Store id for curation requests.
      newResource['id'] = _links.self.href.split('/').pop();

      return fetchAndAdd(newResource, [
        {name: `${registrationSessionType}RegistrationRequest`, url: _links[`${registrationSessionType}RegistrationRequest`].href}
      ], init);
    }));

    dispatch(setRegistrationSessionList(registrationRequests, registrationSessionType));

    return data;
  };
};


// Redux store update for registration session list.
export const setRegistrationSessionList = (registrationSessionList, registrationSessionType) => {
  return {
    type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONLIST`,
    registrationSessionList
  };
};
