//
// config actions.
//

// Get config from devops endpoint.
export const getConfigFromDevopsApi = (url) => {
  return async (dispatch) => {
    let requestUrl = new URL(`${url}/devopsApi/getSpaConfiguration`);

    if (process.env.NODE_ENV === 'development') {
      console.log('fetching config from', requestUrl);
    }

    try {
      const response = await fetch(requestUrl);
      const data = await response.json();

      dispatch(setConfig(data));
    }

    catch (err) {
      console.log('Error fetching config, falling back to defaults.', err);
    }
  }
};


// Redux store update for config.
export const setConfig = (config) => {
  return {
    type: 'SET_CONFIG',
    config
  };
};
