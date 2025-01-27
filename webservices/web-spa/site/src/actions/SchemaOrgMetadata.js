// Utils.
import { appendSchemaOrg } from '../utils/schemaOrg';


//
// SchemaOrgMetadata actions.
//

// Get Schema.org Metadata list from registry. Will dispatch setSchemaOrgMetadata.
export const getSchemaOrgMetadataFromRegistry = (namespaceId) => {
  return async (dispatch, getState) => {
    const config = getState().config;
    let requestUrl = !namespaceId ? `${config.registryApi}/${config.schemaOrgPlatformEndpoint}` : `${config.registryApi}/${config.schemaOrgNamespaceEndpoint}/${namespaceId}`;

    const response = await fetch(requestUrl);
    let json = {};
    if (response.ok) {
      json = await response.json();
    }

    dispatch(setSchemaOrgMetadata(json));
  };
};

// Get Schema.org Metadata list from registry using a prefix. Will dispatch setSchemaOrgMetadata.
export const getSchemaOrgMetadataByPrefixFromRegistry = (prefix) => {
  return async (dispatch, getState) => {
    const config = getState().config;
    let requestUrl = `${config.registryApi}/${config.schemaOrgNamespacePrefixEndpoint}/${prefix}`;

    const response = await fetch(requestUrl);
    let json = {};
    if (response.ok)
      json = response.json();

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