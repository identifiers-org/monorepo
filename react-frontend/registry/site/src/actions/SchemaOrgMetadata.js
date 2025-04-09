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

    let json = {};
    try {
      const response = await fetch(requestUrl);
      json = await response.json();
    } catch (err) {
      console.error(`Failed to fetch ${requestUrl}`, err);
    }

    dispatch(setSchemaOrgMetadata(json));
  };
};

// Get Schema.org Metadata list from registry using a prefix. Will dispatch setSchemaOrgMetadata.
export const getSchemaOrgMetadataByPrefixFromRegistry = (prefix) => {
  return async (dispatch) => {
    let requestUrl = `${config.registryApi}/${config.schemaOrgNamespacePrefixEndpoint}/${prefix}`;

    let json = {};
    try {
      const response = await fetch(requestUrl);
      if (response.ok) {
        json = await response.json();
      }
    } catch (err) {
      console.error(err);
    }

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