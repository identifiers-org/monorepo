import { config } from '../config/Config';

import { fetchAndAdd } from '../utils/fetchAndAdd';
import { setPrefixRegistrationSessionAmend } from './PrefixRegistrationSessionAmend';


//
// PrefixRegistrationSession actions.
//

// Get Prefix Registration Session from registry. Will dispatch setPrefixRegistrationSession.
export const getPrefixRegistrationSessionFromRegistry = (id) => {
  return async (dispatch) => {
    const requestUrl = new URL(`${config.registryApi}/restApi/prefixRegistrationSessions/${id}`);

    try {
      const response = await fetch(requestUrl);
      let data = await response.json();

      // Fetches current prefixRegistrationRequest.
      await fetchAndAdd(data, [
        {name: 'prefixRegistrationRequest', url: data._links.prefixRegistrationRequest.href}
      ]);

      const eventsUrl = `${config.registryApi}/restApi/prefixRegistrationSessionEvents/search/findByPrefixRegistrationSessionId?id=${id}`
      const eventsResponse = await fetch(eventsUrl);

      let eventsData = await eventsResponse.json();

      // Fetch amend event prefixRegistrationRequest.
      if (eventsData._embedded.prefixRegistrationSessionEventAmends) {
        eventsData._embedded.prefixRegistrationSessionEventAmends = await Promise.all(eventsData._embedded.prefixRegistrationSessionEventAmends.map(event => {
          return fetchAndAdd(event, [
            {name: 'prefixRegistrationRequest', url: event._links.prefixRegistrationRequest.href}
          ]);
        }));
      }

      // Also fetch start event prefixRegistrationRequest.
      const event = eventsData._embedded.prefixRegistrationSessionEventStarts[0];
      eventsData._embedded.prefixRegistrationSessionEventStarts[0] = await fetchAndAdd(event, [
        {name: 'prefixRegistrationRequest', url: event._links.prefixRegistrationRequest.href}
      ]);


      // Flatten event list and sort by date.
      let events = Object.keys(eventsData._embedded).map(_ => eventsData._embedded[_]);
      data.prefixRegistrationSessionEvents = events.flat()
      data.prefixRegistrationSessionEvents.sort((a, b) => new Date(b.created) - new Date(a.created));

      // Dispatch redux state update.
      dispatch(setPrefixRegistrationSession(data));

      // Prepare amend template in state as a copy of the request.
      dispatch(setPrefixRegistrationSessionAmend(data.prefixRegistrationRequest))
    }
    catch (err) {
      console.log('Error fetching prefix registration request: ', err);
    };
  };
};


// Redux store update for prefixRegistrationSession.
export const setPrefixRegistrationSession = (prefixRegistrationSession) => {
  return {
    type: 'SET_PREFIXREGISTRATIONSESSION',
    prefixRegistrationSession
  };
};


// Accept prefixRegistrationRequest.
export const prefixRegistrationRequestAccept = (id, reason) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.prefixRegistrationEndpoint}/acceptPrefixRegistrationRequest/${id}`;
    const config = {
      method: 'POST',
      headers: {'content-type': 'application/json'},
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: 'Source: curation web interface',
          acceptanceReason: reason
        }
      })
    };

    return await fetch(requestUrl, config);
  }
};


// Reject prefixRegistrationRequest.
export const prefixRegistrationRequestReject = (id, reason) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.prefixRegistrationEndpoint}/rejectPrefixRegistrationRequest/${id}`;
    const config = {
      method: 'POST',
      headers: {'content-type': 'application/json'},
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: 'Source: curation web interface',
          rejectionReason: reason
        }
      })
    };

    return await fetch(requestUrl, config);
  }
};


// Comment prefixRegistrationRequest.
export const prefixRegistrationRequestComment = (id, comment) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.prefixRegistrationEndpoint}/commentPrefixRegistrationRequest/${id}`;
    const config = {
      method: 'POST',
      headers: {'content-type': 'application/json'},
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: 'Source: curation web interface',
          comment,
        }
      })
    };

    return await fetch(requestUrl, config);
  }
};


// Comment prefixRegistrationRequest.
export const prefixRegistrationRequestAmend = (id, prefixRegistrationRequest, additionalInformation) => {
  console.log('AMEND', id, prefixRegistrationRequest, additionalInformation);
};