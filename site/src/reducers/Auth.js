// Keycloak.
import Keycloak from '../utils/keycloak';
import keycloakConf from '../config/keycloak.json';


// Auth reducer
const defaultState = {
  authenticated: undefined,
  authRenewalIntervalHandler: undefined,
  keycloak: new Keycloak(keycloakConf)
};

const authReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'SET_AUTHENTICATED':
      return {authenticated: action.keycloak.authenticated, keycloak: action.keycloak};

    case 'SET_AUTHRENEWALINTERVALHANDLER':

      return {...state, authRenewalIntervalHandler: setInterval(action.authRenewalIntervalHandler, action.timeout)};

    case 'RESET_AUTHRENEWALINTERVAL':
      clearInterval(state.authRenewalIntervalHandler);
      console.debug('Auth renew interval reset');
      return {...state, authRenewalIntervalHandler: setInterval(action.authRenewalIntervalHandler, action.timeout)};


    default:
      return state;
  }
}


export default authReducer;
