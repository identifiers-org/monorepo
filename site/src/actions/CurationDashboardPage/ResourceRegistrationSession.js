// Config.
import { config } from '../../config/Config';

// Actions.
import { setRegistrationSessionAmend } from './RegistrationSessionAmend';

// Utils.
import { fetchAndAdd } from '../../utils/fetchAndAdd';
import { renewToken } from '../../utils/auth';


//
// ResourceRegistrationSession actions.
//

// Get Resource Registration Session from registry. Will dispatch setResourceRegistrationSession.
export const getResourceRegistrationSessionFromRegistry = (id) => {
  return async (dispatch, getState) => {
    const authToken = await renewToken();
    const requestUrl = new URL(`${config.registryApi}/restApi/resourceRegistrationSessions/${id}`);
    const init = {headers: {'Authorization': `Bearer ${authToken}`}};

    let data;
    try {
      const response = await fetch(requestUrl, init);
      data = await response.json();

      // Fetches current resourceRegistrationRequest.
      await fetchAndAdd(data, [
        {name: 'resourceRegistrationRequest', url: data._links.resourceRegistrationRequest.href}
      ], init);
    }
    catch (err) {
      console.log('Error fetching resource registration request: ', err);
    }

    const eventsUrl = `${config.registryApi}/restApi/resourceRegistrationSessionEvents/search/findByResourceRegistrationSessionId?id=${id}`
    const eventsResponse = await fetch(eventsUrl, init);

    let eventsData = await eventsResponse.json();

    // Fetch amend event resourceRegistrationRequest.
    if (eventsData._embedded.resourceRegistrationSessionEventAmends) {
      eventsData._embedded.resourceRegistrationSessionEventAmends = await Promise.all(eventsData._embedded.resourceRegistrationSessionEventAmends.map(event => {
        return fetchAndAdd(event, [
          {name: 'resourceRegistrationRequest', url: event._links.resourceRegistrationRequest.href}
        ], init);
      }));
    }

    // Also fetch start event resourceRegistrationRequest.
    const event = eventsData._embedded.resourceRegistrationSessionEventStarts[0];
    eventsData._embedded.resourceRegistrationSessionEventStarts[0] = await fetchAndAdd(event, [
      {name: 'resourceRegistrationRequest', url: event._links.resourceRegistrationRequest.href}
    ], init);


    // Flatten event list and sort by date.
    let events = Object.keys(eventsData._embedded).map(_ => eventsData._embedded[_]);
    data.resourceRegistrationSessionEvents = events.flat()
    data.resourceRegistrationSessionEvents.sort((a, b) => new Date(b.created) - new Date(a.created));

    // Dispatch redux state update.
    dispatch(setResourceRegistrationSession(data));

    // Prepare amend template in state as a copy of the request.
    dispatch(setRegistrationSessionAmend(data.resourceRegistrationRequest, 'resource'))
  };
};


// Redux store update for resourceRegistrationSession.
export const setResourceRegistrationSession = (resourceRegistrationSession) => {
  return {
    type: 'SET_RESOURCEREGISTRATIONSESSION',
    resourceRegistrationSession
  };
};


// Accept resourceRegistrationRequest.
export const resourceRegistrationRequestAccept = (id, reason) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.resourceRegistrationEndpoint}/acceptResourceRegistrationRequest/${id}`;
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


// Reject resourceRegistrationRequest.
export const resourceRegistrationRequestReject = (id, reason) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.resourceRegistrationEndpoint}/rejectResourceRegistrationRequest/${id}`;
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


// Comment resourceRegistrationRequest.
export const resourceRegistrationRequestComment = (id, comment) => {
  return async (undefined, getState) => {
    const requestUrl = `${config.registryApi}/${config.resourceRegistrationEndpoint}/commentResourceRegistrationRequest/${id}`;
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


// Amend resourceRegistrationRequest.
export const resourceRegistrationRequestAmend = (id, resourceRegistrationRequest, additionalInformation) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.resourceRegistrationEndpoint}/amendResourceRegistrationRequest/${id}`;

    // Supporting references must be an array to talk to the API. It is split here by commas. It is not ideal, but will stay like this
    // until we find a better solution.
    resourceRegistrationRequest.supportingReferences = resourceRegistrationRequest.supportingReferences.split(',');

    // Also, requester data must be inside a subobject.
    resourceRegistrationRequest['requester'] = {name: resourceRegistrationRequest.requesterName, email: resourceRegistrationRequest.requesterEmail};

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
          resourceRegistrationRequest
        }
      })
    };

    return await fetch(requestUrl, init);
  }
};