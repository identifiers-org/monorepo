// Config.
import { config } from '../config/Config';

// Utils.
import { renewToken } from '../utils/auth';


//
// ResourcePatch actions.
//

// Redux store update for resource patch fields.
export const setResourcePatchField = (field, value) => {
  const action = {
    type: 'SET_RESOURCEPATCHREDUCERFIELD',
    field,
    value
  };

  return action;
};


// Redux store update for whole resource patch.
export const setResourcePatch = (id, resource) => {
  const action = {
    type: 'SET_RESOURCEPATCHREDUCER',
    id,
    resource
  }

  return action;
};


// Patch a resource.
export const patchResource = (id, newResource) => {
  return async () => {
    const requestUrl = `${config.registryApi}/restApi/resources/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'PATCH',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify(newResource)
    };

    return await fetch(requestUrl, init);
  }
};