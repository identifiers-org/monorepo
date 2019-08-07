// Config.
import { config } from '../../config/Config';

// Actions.
import { setPrefixRegistrationSessionAmend } from './PrefixRegistrationSessionAmend';

// Utils.
import { fetchAndAdd } from '../../utils/fetchAndAdd';
import { renewToken } from '../../utils/auth';


//
// PrefixRegistrationSession actions.
//

// Get Prefix Registration Session from registry. Will dispatch setPrefixRegistrationSession.
export const getPrefixRegistrationSessionFromRegistry = (id) => {
  return async (dispatch, getState) => {
    const authToken = await renewToken();
    const requestUrl = new URL(`${config.registryApi}/restApi/prefixRegistrationSessions/${id}`);
    const init = {headers: {'Authorization': `Bearer ${authToken}`}};

    try {
      const response = await fetch(requestUrl, init);
      let data = await response.json();

      // Fetches current prefixRegistrationRequest.
      await fetchAndAdd(data, [
        {name: 'prefixRegistrationRequest', url: data._links.prefixRegistrationRequest.href}
      ], init);

      const eventsUrl = `${config.registryApi}/restApi/prefixRegistrationSessionEvents/search/findByPrefixRegistrationSessionId?id=${id}`
      const eventsResponse = await fetch(eventsUrl, init);

      let eventsData = await eventsResponse.json();

      // Fetch amend event prefixRegistrationRequest.
      if (eventsData._embedded.prefixRegistrationSessionEventAmends) {
        eventsData._embedded.prefixRegistrationSessionEventAmends = await Promise.all(eventsData._embedded.prefixRegistrationSessionEventAmends.map(event => {
          return fetchAndAdd(event, [
            {name: 'prefixRegistrationRequest', url: event._links.prefixRegistrationRequest.href}
          ], init);
        }));
      }

      // Also fetch start event prefixRegistrationRequest.
      const event = eventsData._embedded.prefixRegistrationSessionEventStarts[0];
      eventsData._embedded.prefixRegistrationSessionEventStarts[0] = await fetchAndAdd(event, [
        {name: 'prefixRegistrationRequest', url: event._links.prefixRegistrationRequest.href}
      ], init);


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
    }
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
    const authToken = await renewToken();
    const init = {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: 'Source: curation web interface',
          acceptanceReason: reason
        }
      })
    };

    return await fetch(requestUrl, init);
  }
};


// Reject prefixRegistrationRequest.
export const prefixRegistrationRequestReject = (id, reason) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.prefixRegistrationEndpoint}/rejectPrefixRegistrationRequest/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: 'Source: curation web interface',
          rejectionReason: reason
        }
      })
    };

    return await fetch(requestUrl, init);
  }
};


// Comment prefixRegistrationRequest.
export const prefixRegistrationRequestComment = (id, comment) => {
  return async (undefined, getState) => {
    const requestUrl = `${config.registryApi}/${config.prefixRegistrationEndpoint}/commentPrefixRegistrationRequest/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: 'Source: curation web interface',
          comment
        }
      })
    };

    return await fetch(requestUrl, init);
  }
};


// Amend prefixRegistrationRequest.
export const prefixRegistrationRequestAmend = (id, prefixRegistrationRequest, additionalInformation) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.prefixRegistrationEndpoint}/amendPrefixRegistrationRequest/${id}`;

    // Supporting references must be an array to talk to the API. It is split here by commas. It is not ideal, but will stay like this
    // until we find a better solution.
    prefixRegistrationRequest.supportingReferences = prefixRegistrationRequest.supportingReferences.split(',');

    // Also, requester data must be inside a subobject.
    prefixRegistrationRequest['requester'] = {name: prefixRegistrationRequest.requesterName, email: prefixRegistrationRequest.requesterEmail};

    const authToken = await renewToken();
    const init = {
      method: 'POST',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify({
        apiVersion: config.apiVersion,
        payload: {
          additionalInformation: additionalInformation || 'Source: curation web interface',
          prefixRegistrationRequest
        }
      })
    };

    return await fetch(requestUrl, init);
  }
};