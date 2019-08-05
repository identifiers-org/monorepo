import { config } from '../config/Config';


async function validateThroughAPI (url, payload) {
  const body = {
    apiVersion: 1,
    payload
  };

  const init = {
    method: 'POST',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify(body)
  };

  const response = await fetch(url, init);
  const responseStatusCode = response.status;
  const json = await response.json();
  return {
    valid: responseStatusCode === 200,
    errorMessage: json.errorMessage
  };
}


const validators = {
  name: async (name) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateName`;
    return validateThroughAPI(url, {name});
  },

  description: async (description) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateDescription`;
    return validateThroughAPI(url, {description});
  },

  requestedPrefix: async (requestedPrefix) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateRequestedPrefix`;
    return validateThroughAPI(url, {requestedPrefix});
  },

  // TODO: Fix when validators are refactored in backend. Hack: if no url pattern is present, all sampleIds are ok.
  sampleId: async (sampleId, prefixRegistrationRequest) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateSampleId`;
    if (typeof prefixRegistrationRequest.providerUrlPattern === 'undefined') {
      return {valid: true};
    }
    return validateThroughAPI(url, {sampleId, providerUrlPattern: prefixRegistrationRequest.providerUrlPattern});
  },

  idRegexPattern: async (idRegexPattern, prefixRegistrationRequest) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateIdRegexPattern`;
    return validateThroughAPI(url, {idRegexPattern, sampleId: prefixRegistrationRequest.sampleId});
  },

  pattern: async (idRegexPattern, prefixRegistrationRequest) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateIdRegexPattern`;
    return validateThroughAPI(url, {idRegexPattern, sampleId: prefixRegistrationRequest.sampleId});
  },

  supportingReferences: async (supportingReferences) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateReferences`;
    return validateThroughAPI(url, {supportingReferences});
  },

  additionalInformation: async (additionalInformation) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateAdditionalInformation`;
    return validateThroughAPI(url, {additionalInformation});
  },

  institution: () => {valid: true},

  institutionName: async (institutionName) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateInstitutionName`;
    return validateThroughAPI(url, {institutionName});
  },

  institutionDescription: async (institutionDescription) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateInstitutionDescription`;
    return validateThroughAPI(url, {institutionDescription});
  },

  institutionHomeUrl: async (institutionHomeUrl) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateInstitutionHomeUrl`;
    return validateThroughAPI(url, {institutionHomeUrl});
  },

  institutionLocation: async (institutionLocation) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateInstitutionLocation`;
    return validateThroughAPI(url, {institutionLocation});
  },

  namespaceEmbeddedInLui: async (namespaceEmbeddedInLui) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateNamespaceEmbeddedInLui`;
    return validateThroughAPI(url, {namespaceEmbeddedInLui});
  },

  providerName: async (providerName) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderName`;
    return validateThroughAPI(url, {providerName});
  },

  providerDescription: async (providerDescription) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderDescription`;
    return validateThroughAPI(url, {providerDescription});
  },

  providerHomeUrl: async (providerHomeUrl) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderHomeUrl`;
    return validateThroughAPI(url, {providerHomeUrl});
  },

  resourceHomeUrl: async (providerHomeUrl) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderHomeUrl`;
    return validateThroughAPI(url, {providerHomeUrl});
  },

  providerCode: async (providerCode) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderCode`;
    return validateThroughAPI(url, {providerCode});
  },

  providerUrlPattern: async (providerUrlPattern) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderUrlPattern`;
    return validateThroughAPI(url, {providerUrlPattern});
  },

  urlPattern: async (providerUrlPattern) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderUrlPattern`;
    return validateThroughAPI(url, {providerUrlPattern});
  },

  providerLocation: async (providerLocation) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateProviderLocation`;
    return validateThroughAPI(url, {providerLocation});
  },

  requesterName: async (requesterName) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateRequesterName`;
    return validateThroughAPI(url, {requester: {name: requesterName}});
  },

  requesterEmail: async (requesterEmail) => {
    const url = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/validateRequesterEmail`;
    return validateThroughAPI(url, {requester: {email: requesterEmail}});
  }
};


export default validators;
