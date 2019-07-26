// Init Keycloak object. Will dispatch setAuthenticated.
export const authInit = () => {
  return async (dispatch, getState) => {
    const { keycloak } = getState().auth;

    try {
      await keycloak.init({promiseType: 'native', onLoad: 'check-sso'});
    } catch {
      console.error('Unable to authenticate.');
    }

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

    await keycloak.logout();
    dispatch(setAuthenticated(keycloak));
  };
};


// Redux store update for auth credentials.
export const setAuthenticated = (keycloak) => {
  return {
    type: 'SET_AUTHENTICATED',
    keycloak
  };
};


// Setting of the authentication interval handler into redux store.
export const saveAuthRenewalIntervalHandler = (authRenewalIntervalHandler, timeout) => {
  return {
    type: 'SET_AUTHRENEWALINTERVALHANDLER',
    authRenewalIntervalHandler,
    timeout
  }
}

// Reset of the authentication interval.
export const resetAuthRenewalInterval = () => {
  return {
    type: 'RESET_AUTHRENEWALINTERVAL'
  }
}
