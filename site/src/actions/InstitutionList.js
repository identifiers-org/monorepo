// Config.
import { config } from '../config/Config';

// Utils.
import { fetchAndAdd } from '../utils/fetchAndAdd';


//
// InstitutionList actions.
//

// Get institutions list from registry. Will dispatch setInstitutionList.
// TODO: This gets the first 1000. Should paginate requests to get all institutions in case the total
// amount of institutions grow above 1000.
export const getInstitutionListFromRegistry = () => {
  return async (dispatch) => {
    let requestUrl = config.registryApi + '/restApi/institutions?size=1000';

    const response = await fetch(requestUrl);
    const json = await response.json();
    const institutions = json._embedded.institutions.map(institution => ({
      id: institution._links.self.href,
      shortId: institution._links.self.href.split('/').pop(),
      name: institution.name,
      homeUrl: institution.homeUrl,
      description: institution.description
    }));

    dispatch(setInstitutionList(institutions));
  };
};

// Get a single institutions from registry, including its hateoas links. Will dispatch setInstitution.
export const getInstitutionFromRegistry = (id) => {
  return async (dispatch) => {
    let requestUrl = config.registryApi + `/restApi/institutions/${id}`;

    const response = await fetch(requestUrl);
    const institution = await response.json();

    let newInstitution = {
      id: institution._links.self.href,
      shortId: institution._links.self.href.split('/').pop(),
      name: institution.name,
      homeUrl: institution.homeUrl,
      description: institution.description
    };

    newInstitution = await fetchAndAdd(newInstitution, [
      {name: 'location', url: institution._links.location.href}
    ], undefined, true);

    dispatch(setInstitution(newInstitution));

    return newInstitution;
  };
};


// Get institution using a ROR ID.
export const getInstitutionForRORIDFromRegistry = (rorId) => async () => {
  let requestURL = new URL(`${config.registryApi}/${config.rorIdEndpoint}/getInstitutionForRorId`);
  const init = {
    method: 'POST',
    headers: {
      'content-type': 'application/json',
    },
    body: JSON.stringify({
      apiVersion: '1.0',
      payload: {rorId}
    })
  };

  const response = await fetch(requestURL, init);

  return response.status === 200 ? await response.json() : null;
};


export const setInstitutionList = (institutionList) => {
  return {
    type: 'SET_INSTITUTIONLIST',
    institutionList
  };
};

export const setInstitution = (institution) => {
  return {
    type: 'SET_INSTITUTION',
    institution
  };
};