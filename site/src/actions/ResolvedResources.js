//
// ResolvedResources actions.
//

// Resolve a compact identifier. Will dispatch setResolvedResources.
export const getResolvedResources = (query) => {
  return async (dispatch, getState) => {
    const config = getState().config;
    let requestUrl = new URL(`${config.resolverApi}/${query}`);

    let response;
    let data;

    try {
      response = await fetch(requestUrl);
      data = await response.json();
    }
    catch (err) {
      console.error('Error fetching namespaces: ', err);
    }

    // Sort resources by their score.
    data = data.payload.resolvedResources.sort((a, b) => b.recommendation.recommendationIndex - a.recommendation.recommendationIndex);

    // Add compact identifier to every resource.
    data.forEach(resolvedResource => {
      resolvedResource['compactIdentifier'] = `${config.resolverApi}/${resolvedResource.providerCode}/${query}`
    });

    dispatch(setResolvedResources(data));

    return response;
  };
};


// Redux store update for resolvedResources data.
export const setResolvedResources = (resolvedResources) => {
  return {
    type: 'SET_RESOLVEDRESOURCES',
    resolvedResources
  };
};