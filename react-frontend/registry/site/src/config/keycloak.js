// Config.
import { config } from './Config';


export const keycloakConf = {
  'realm': 'idorg',
  'url': config.authApi,
  'ssl-required': 'external',
  'resource': 'hq-web-frontend',
  'public-client': true,
  'confidential-port': 0,
  'clientId': 'hq-web-frontend',
  'redirectUri': config.authRedirectUri
};
