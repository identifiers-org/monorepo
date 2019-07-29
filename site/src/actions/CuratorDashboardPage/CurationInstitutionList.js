// Actions.
import { setCurationInstitutionListParams } from './CurationInstitutionListParams';

// Config.
import { config } from '../../config/Config';


//
// CurationInstitutionList actions.
//

// Get institutions list from registry. Will dispatch setCuratorInstitutionList.
export const getCurationInstitutionListFromRegistry = (params) => {
  return async (dispatch) => {
    let requestURL;

    if (params.nameContent && params.nameContent.length > 2) {
      requestURL = new URL(config.registryApi + '/restApi/institutions/search/findByNameContaining');
    } else {
      requestURL = new URL(config.registryApi + '/restApi/institutions');
    }

    Object.keys(params).forEach(param => {
      requestURL.searchParams.append(param, params[param]);
    });

    const response = await fetch(requestURL);
    const json = await response.json();

    await dispatch(setCurationInstitutionListParams(json.page));

    const institutions = json._embedded.institutions.map(institution => ({
      id: institution._links.self.href,
      name: institution.name,
      homeUrl: institution.homeUrl,
      description: institution.description
    }));

    dispatch(setCurationInstitutionList(institutions));
  };
};


export const setCurationInstitutionList = (curationInstitutionList) => {
  return {
    type: 'SET_CURATIONINSTITUTIONLIST',
    curationInstitutionList
  }
}
