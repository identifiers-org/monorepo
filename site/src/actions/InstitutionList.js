import { config } from '../config/Config';


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
      id: institution._links.self.href, //.split('/').pop(),
      name: institution.name,
      homeUrl: institution.homeUrl,
      description: institution.description
    })).sort((a, b) => a.name < b.name ? -1 : a.name > b.name ? 1 : 0);

    dispatch(setInstitutionList(institutions));
  };
};


export const setInstitutionList = (institutionList) => {
  return {
    type: 'SET_INSTITUTIONLIST',
    institutionList
  }
}