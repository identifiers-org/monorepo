// Config.
import { config } from '../../config/Config';

// Utils.
import { renewToken } from '../../utils/auth';


//
// institutionPatch actions.
//

// Redux store update for institution patch fields.
export const setInstitutionPatchField = (field, value) => {
  const action = {
    type: 'SET_INSTITUTIONPATCHREDUCERFIELD',
    field,
    value
  };

  return action;
};


// Redux store update for whole institution patch.
export const setInstitutionPatch = (id, institution) => {
  const action = {
    type: 'SET_INSTITUTIONPATCHREDUCER',
    id,
    institution
  }

  return action;
};


// Patch a institution.
export const patchInstitution = (id, newInstitution) => {
  return async () => {
    const requestUrl = `${config.registryApi}/restApi/institutions/${id}`;
    const authToken = await renewToken();
    const init = {
      method: 'PATCH',
      headers: {
        'content-type': 'application/json',
        'Authorization': `Bearer ${authToken}`
      },
      body: JSON.stringify(newInstitution)
    };

    return await fetch(requestUrl, init);
  }
};