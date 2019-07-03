// Init Keycloak object. Will dispatch setAuthenticated.
export const authInit = () => {
  return async (dispatch, getState) => {
    const { keycloak } = getState().auth;

    await keycloak.init({promiseType: 'native', onLoad: 'check-sso'});
    dispatch(setAuthenticated(keycloak));
  };
};

// Actively login in Keycloak. Will dispatch setAuthenticated.
export const doSignIn = () => {
  return async (dispatch, getState) => {
    const { keycloak } = getState().auth;

    await keycloak.login();
    dispatch(setAuthenticated(keycloak));
  };
};

// Actively logout in Keycloak. Will dispatch setAuthenticated.
export const doSignOut = () => {
  return async (dispatch, getState) => {
    const { keycloak } = getState().auth;

    keycloak.logout().success(authenticated => {
      dispatch(setAuthenticated(keycloak));
    })
  };
};


// Redux store update for auth credentials.
export const setAuthenticated = (keycloak) => {
  return {
    type: 'SET_AUTHENTICATED',
    keycloak
  };
};
