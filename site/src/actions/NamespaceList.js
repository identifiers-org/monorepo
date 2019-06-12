import { fetchAndAdd } from '../utils/fetchAndAdd';


//
// namespaceList actions.
//

// Get namespaces from registry. Will dispatch setNamespaceList.
export const getNamespacesFromRegistry = (query) => {
  return async (dispatch, getState) => {
    const config = getState().config;
    let requestUrl = new URL(`${config.registryApi}/restApi/namespaces/search/findByPrefixContaining`);

    // Params to add to request: CONTENT, SIZE, SORT
    requestUrl.searchParams.append('content', query === '' ? 'nothingtosearch' : query);
    requestUrl.searchParams.append('size', config.suggestionQuerySize);
    requestUrl.searchParams.append('sort', 'name,asc');

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();

      dispatch(setNamespaceList(data._embedded.namespaces));

      // Get resources for those namespaces if resource prediction is enabled.
      if (config.enableResourcePrediction) {
        data._embedded.namespaces.map(async namespace => {
          await dispatch(getResourcesFromRegistry(namespace));
        });
      }

      return response;
    }
    catch (err) {
      console.error('Error fetching namespaces: ', err);
    }
  };
};


// Get resources from registry. Will dispatch setResources.
export const getResourcesFromRegistry = (namespace) => {
  return async (dispatch, getState) => {
    const config = getState().config;
    let requestUrl = new URL(`${config.registryApi}/restApi/resources/search/findAllByNamespaceId`);
    requestUrl.searchParams.append('id', namespace._links.self.href.split('/').pop());

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();
      const resources = await Promise.all(data._embedded.resources.map(resource => {
        let { _links, ...newResource } = resource;

        return fetchAndAdd(newResource, [
          {name: 'institution', url: _links.institution.href},
          {name: 'location', url: _links.location.href}
        ]);
      }));

      dispatch(setResources(namespace.prefix, resources));
    }
    catch (err) {
      console.error('Error fetching namespaces: ', err);
    };
  };
};


// Redux store update for namespace list.
export const setNamespaceList = (namespaceList) => {
  return {
    type: 'SET_NAMESPACELIST',
    namespaceList
  };
};

export const setResources = (prefix, resources) => {
  return {
    type: 'SET_RESOURCES',
    prefix,
    resources
  }
}