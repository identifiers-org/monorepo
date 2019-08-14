import { config } from '../config/Config';


//
// LocationList actions.
//

// Get Locations list from registry. Will dispatch setLocationList.
export const getLocationListFromRegistry = () => {
  return async (dispatch) => {
    let requestUrl = config.registryApi + '/restApi/locations?size=1000';

    const response = await fetch(requestUrl);
    const json = await response.json();
    const locations = json._embedded.locations.map(location => ({
      id: location._links.self.href, //.split('/').pop(),
      label: location.countryName
    })).sort((a, b) => a.name > b.name ? -1 : a.name < b.name ? 1 : 0);

    dispatch(setLocationList(locations));
  };
};


export const setLocationList = (locationList) => {
  return {
    type: 'SET_LOCATIONLIST',
    locationList
  }
}