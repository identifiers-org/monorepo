export const config = {
  resolverApi: 'http://localhost:8080',
  resolverHardcodedUrl: 'https://identifiers.org',
  registryApi: 'http://localhost:8180',

  registryUrl: 'https://registry.identifiers.org',
  registryPrefixRegistrationRequestFormUrl: 'https://registry.identifiers.org/prefixregistrationrequest',

  feedbackUrl: 'https://github.com/identifiers-org/identifiers-org.github.io/issues/new',

  oldIdentifiersUrl: 'https://ebi.identifiers.org',

  apiVersion: '1.0',

  // This requires a working HQ architecture with a central registry.
  showSearchSuggestions: true,

  // We don't predict resources for now.
  enableResourcePrediction: false
};
