// Config.
import { config } from '../config/Config';


//
// SchemaOrgMetadata actions.
//

// Get Schema.org Metadata list from registry. Will dispatch setSchemaOrgMetadata.
export const getSchemaOrgMetadataFromRegistry = (id) => {
  return async (dispatch) => {
    let requestUrl = !id ? config.schemaOrgPlatformEndpoint : `${config.schemaOrgNamespaceEndpoint}/${id}`;

    const response = await fetch(requestUrl);

    console.log('response', response);

    const json = await response.json();

    console.log('json', json);


    dispatch(setLocationList(json));
  };
};


export const setLocationList = (schemaOrgMetadata) => {
  return {
    type: 'SET_SCHEMAORGMETADATA',
    schemaOrgMetadata
  }
}