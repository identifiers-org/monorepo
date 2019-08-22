// Actions.
import { setCurationInstitutionListParams } from './CurationInstitutionListParams';

// Config.
import { config } from '../../config/Config';

// Utils.
import { fetchAndAdd } from '../../utils/fetchAndAdd';
import { renewToken } from '../../utils/auth';


//
// CurationInstitutionList actions.
//

// Get institutions list from registry. Will dispatch setCurationInstitutionList.
export const getCurationInstitutionListFromRegistry = (params) => {
  return async (dispatch) => {
    let requestURL;

    if (params.nameContent && params.nameContent.length > 2) {
      requestURL = new URL(config.registryApi + '/restApi/institutions/search/findByNameContaining');
    } else {
      requestURL = new URL(config.registryApi + '/restApi/institutions');
      params['sort'] = 'name,asc';
    }

    Object.keys(params).forEach(param => {
      requestURL.searchParams.append(param, params[param]);
    });

    const response = await fetch(requestURL);
    const json = await response.json();

    await dispatch(setCurationInstitutionListParams(json.page));

    const institutions = await Promise.all(json._embedded.institutions.map(institution => {
      let { _links, ...newInstitution } = institution;

      // Add id to the resource as a field, we will need this for curation stuff.
      newInstitution.id = _links.self.href.split('/').pop();

      return fetchAndAdd(newInstitution, [
        {name: 'location', url: _links.location.href}
      ], undefined, true);
    }));

    dispatch(setCurationInstitutionList(institutions));
  };
};


// Get institution using a ROR ID.
export const getInstitutionForRORIDFromRegistry = (rorId) => {
  return async (dispatch) => {
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

    return response.status !== 200 ? null : await response.json();
  };
};


// Delete an institution from the registry.
export const deleteInstitutionFromRegistry = (institutionId) => {
  return async (dispatch) => {
    const authToken = await renewToken();
    const init = {
      headers: {Authorization: `Bearer ${authToken}`}
    };
    let requestURL = new URL(`${config.registryApi}/${config.institutionManagementEndpoint}/deleteById/${institutionId}`);

    const response = await fetch(requestURL, init);
    let namespacesUsingInstitution = [];

    // If unable to delete, determine which namespaces are using that institution and return them.
    if (response.status === 400) {
      let namespacesUsingInstitutionRequestURL = new URL(`${config.registryApi}/restApi/resources/search/findAllByInstitutionId?id=${institutionId}`);

      const resourcesUsingInstitutionResponse =  await fetch(namespacesUsingInstitutionRequestURL);
      const resourcesUsingInstitutionData = await resourcesUsingInstitutionResponse.json();
      const resourcesUsingInstitution = await Promise.all(resourcesUsingInstitutionData._embedded.resources.map(resource => {
        let { _links, ...newResource } = resource;

        return fetchAndAdd(newResource, [
          {name: 'namespace', url: _links.namespace.href}
        ], undefined, true);
      }));

      namespacesUsingInstitution = resourcesUsingInstitution.map(resource => ({
        name: resource.namespace.name,
        prefix: resource.namespace.prefix
      }));
    }

    return {
      status: response.status,
      namespacesUsingInstitution
    }
  };
};


export const setCurationInstitutionList = (curationInstitutionList) => {
  return {
    type: 'SET_CURATIONINSTITUTIONLIST',
    curationInstitutionList
  };
};
