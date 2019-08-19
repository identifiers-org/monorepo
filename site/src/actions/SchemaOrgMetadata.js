// Config.
import { config } from '../config/Config';

// Utils.
import { appendSchemaOrg } from '../utils/schemaOrg';


//
// SchemaOrgMetadata actions.
//

// Get Schema.org Metadata list from registry. Will dispatch setSchemaOrgMetadata.
export const getSchemaOrgMetadataFromRegistry = (namespaceId) => {
  return async (dispatch) => {
    let requestUrl = !namespaceId ? `${config.registryApi}/${config.schemaOrgPlatformEndpoint}` : `${config.registryApi}/${config.schemaOrgNamespaceEndpoint}/${namespaceId}`;

    const response = await fetch(requestUrl);
    const json = await response.json();

    dispatch(setSchemaOrgMetadata(json));
  };
};


export const setSchemaOrgMetadata = (schemaOrgMetadata) => {
  // Inject metadata in DOM.
  appendSchemaOrg(schemaOrgMetadata);

  return {
    type: 'SET_SCHEMAORGMETADATA',
    schemaOrgMetadata
  };
}