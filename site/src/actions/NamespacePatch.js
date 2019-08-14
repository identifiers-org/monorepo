// Config.
import { config } from '../config/Config';

// Utils.
import { renewToken } from '../utils/auth';


//
// NamespacePatch actions.
//

// Redux store update for namespace patch fields.
export const setNamespacePatchField = (field, value) => {
  const action = {
    type: 'SET_NAMESPACEPATCHREDUCERFIELD',
    field,
    value
  };

  return action;
};


// Redux store update for whole namespace patch.
export const setNamespacePatch = (id, namespace) => {
  const action = {
    type: 'SET_NAMESPACEPATCHREDUCER',
    id,
    namespace
  };

  return action;
};


// Patch a namespace.
export const patchNamespace = (id, newNamespace) => {
  return async () => {
    const requestUrl = `${config.registryApi}/restApi/namespaces/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'PATCH',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify(newNamespace)
    };

    return await fetch(requestUrl, init);
  };
};


// Deactivate a namespace.
export const deactivateNamespace = (id) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.namespaceManagementEndpoint}/deactivateNamespace/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${authToken}`
      }
    };

    return await fetch(requestUrl, init);
  };
};


// Reactivate a namespace.
export const reactivateNamespace = (id) => {
  return async () => {
    const requestUrl = `${config.registryApi}/${config.namespaceManagementEndpoint}/reactivateNamespace/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${authToken}`
      }
    };

    return await fetch(requestUrl, init);
  };
};
