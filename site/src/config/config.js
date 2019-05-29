export const Config = {
  registryApi: typeof configApiRegistryUrl !== 'undefined' ? configApiRegistryUrl : 'http://localhost:8180',

  validationEndpoint: 'prefixRegistrationApi',
  prefixRegistrationEndpoint: 'prefixRegistrationApi',
  prefixRequestEndpoint: 'prefixRegistrationApi/registerPrefix',

  baseUrl: window.location.protocol + '//' + window.location.hostname + ':' + location.port + '/',

  apiVersion: '1.0',

  VALIDATION_DELAY: 1000,

  enableAuthFeatures: true
};
