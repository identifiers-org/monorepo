// PrefixRegistratonSessionAmend reducer

const defaultState = {
  name: undefined,
  description: undefined,
  requestedPrefix: undefined,
  sampleId: undefined,
  idRegexPattern: undefined,
  supportingReferences: undefined,
  additionalInformation: undefined,
  institutionName: undefined,
  institutionDescription: undefined,
  institutionHomeUrl: undefined,
  institutionLocation: undefined,
  namespaceEmbeddedInLui: undefined,
  providerName: undefined,
  providerDescription: undefined,
  providerHomeUrl: undefined,
  providerCode: undefined,
  providerUrlPattern: undefined,
  providerLocation: undefined,
  requesterName: undefined,
  requesterEmail: undefined
};

const PrefixRegistrationSessionAmendReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_PREFIXREGISTRATIONSESSIONAMENDFIELD': {
    return { ...state, [action.field]: action.value };
  }

  case 'SET_PREFIXREGISTRATIONSESSIONAMEND': {
    return {
      ...state,
      name: action.prefixRegistrationSessionAmend.name,
      description: action.prefixRegistrationSessionAmend.description,
      requestedPrefix: action.prefixRegistrationSessionAmend.requestedPrefix,
      sampleId: action.prefixRegistrationSessionAmend.sampleId,
      idRegexPattern: action.prefixRegistrationSessionAmend.idRegexPattern,
      supportingReferences: action.prefixRegistrationSessionAmend.supportingReferences,
      additionalInformation: action.prefixRegistrationSessionAmend.additionalInformation,
      institutionName: action.prefixRegistrationSessionAmend.institutionName,
      institutionDescription: action.prefixRegistrationSessionAmend.institutionDescription,
      institutionHomeUrl: action.prefixRegistrationSessionAmend.institutionHomeUrl,
      institutionLocation: action.prefixRegistrationSessionAmend.institutionLocation,
      namespaceEmbeddedInLui: action.prefixRegistrationSessionAmend.namespaceEmbeddedInLui,
      providerName: action.prefixRegistrationSessionAmend.providerName,
      providerDescription: action.prefixRegistrationSessionAmend.providerDescription,
      providerHomeUrl: action.prefixRegistrationSessionAmend.providerHomeUrl,
      providerCode: action.prefixRegistrationSessionAmend.providerCode,
      providerUrlPattern: action.prefixRegistrationSessionAmend.providerUrlPattern,
      providerLocation: action.prefixRegistrationSessionAmend.providerLocation,
      requesterName: action.prefixRegistrationSessionAmend.requesterName,
      requesterEmail: action.prefixRegistrationSessionAmend.requesterEmail
    };
  }

  default:
    return state;
  }
}


export default PrefixRegistrationSessionAmendReducer;
