// Keycloak.
import Keycloak from '../utils/keycloak';
import keycloakConf from '../config/keycloak.json';


// Auth reducer
const defaultState = {
  authenticated: undefined,
  keycloak: new Keycloak(keycloakConf)
};

const authReducer = (state = defaultState, action) => {
  switch (action.type) {
    case 'SET_AUTHENTICATED':
      return {authenticated: action.keycloak.authenticated, keycloak: action.keycloak}

    default:
      return state;
  }
}


export default authReducer;
