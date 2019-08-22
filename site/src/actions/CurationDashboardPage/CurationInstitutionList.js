// Actions.
import { setCurationInstitutionListParams } from './CurationInstitutionListParams';

// Config.
import { config } from '../../config/Config';

// Utils.
import { fetchAndAdd } from '../../utils/fetchAndAdd';


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


// Delete an institution from the registry.
export const deleteInstitutionFromRegistry = (institutionId) => {
  return async (dispatch) => {
    let requestURL = new URL(config.registryApi + `/institutionManagementApi/deleteById/${institutionId}`);

    const response = await fetch(requestURL);

    return {
      status: response.status
      // TODO: add field for namespaces using that institution.
    }
  };
};


export const setCurationInstitutionList = (curationInstitutionList) => {
  return {
    type: 'SET_CURATIONINSTITUTIONLIST',
    curationInstitutionList
  };
};
