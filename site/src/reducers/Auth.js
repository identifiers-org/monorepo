// Keycloak.
import Keycloak from '../utils/keycloak';
import { keycloakConf } from '../config/keycloak.js';


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

    default:
      return state;
  }
}


export default authReducer;
