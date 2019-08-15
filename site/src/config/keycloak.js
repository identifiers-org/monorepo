export const keycloakConf = {
  'realm': 'idorg',
  'url': typeof configAuthUrl !== 'undefined' ? configAuthUrl : 'http://localkeycloak:8080/auth',
  'ssl-required': 'external',
  'resource': 'hq-web-frontend',
  'public-client': true,
  'confidential-port': 0,
  'clientId': 'hq-web-frontend',
  'redirectUri': typeof configAuthRedirectUri !== 'undefined' ? configAuthRedirectUri : 'http://127.0.0.1:8182/'
};
