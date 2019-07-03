export const config = {
  registryApi: typeof configApiRegistryUrl !== 'undefined' ? configApiRegistryUrl : 'http://localhost:8180',

  validationEndpoint: 'prefixRegistrationApi',
  prefixRegistrationEndpoint: 'prefixRegistrationApi',
  prefixRequestEndpoint: 'prefixRegistrationApi/registerPrefix',

  satelliteUrl: 'https://identifiers.org',
  feedbackUrl: 'https://github.com/identifiers-org/identifiers-org.github.io/issues/new',
  documentationUrl: 'http://docs.identifiers.org',

  oldIdentifiersUrl: 'https://ebi.identifiers.org',

  showBetaBanner: false,

  suggestionListSize: 10,

  baseUrl: window.location.protocol + '//' + window.location.hostname + ':' + location.port + '/',

  apiVersion: '1.0',

  VALIDATION_DELAY: 1000,

  enableAuthFeatures: true
};
