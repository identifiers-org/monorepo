import moment from 'moment';

// Actions.
import { resetAuthRenewalInterval } from '../actions/Auth';

// Store.
import store from '../store/store';


export const renewToken = async function() {
  const { auth } = store.getState();

  if (!auth || !auth.keycloak.token) {
    console.debug('Auth -> Not authenticated.');
    return undefined;
  }

  const tokenExpired = auth.keycloak.isTokenExpired();
  const tokenIssuedBy = moment.unix(auth.keycloak.tokenParsed.iat);
  const tokenExpiresBy = moment.unix(auth.keycloak.tokenParsed.exp);
  const tokenDuration = (tokenExpiresBy - tokenIssuedBy) / 1000;
  const tokenSecondsRemaining = moment.duration(tokenExpiresBy.diff(moment())).asSeconds();
  const tokenStatusCaption = tokenExpired ? 'Token is expired' : `Token expires in ${tokenSecondsRemaining} seconds`;

  // Do not renew too often.
  if (tokenSecondsRemaining > tokenDuration * .75) {
    return auth.keycloak.token;
  }

  console.debug(`Auth -> ${tokenStatusCaption}. Issuing renewal (${tokenDuration} seconds)...`);

  auth.keycloak.updateToken(tokenDuration);

  store.dispatch(resetAuthRenewalInterval());

  return auth.keycloak.token;
};
