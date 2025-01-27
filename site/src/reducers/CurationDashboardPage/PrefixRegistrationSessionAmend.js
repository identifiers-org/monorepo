// PrefixRegistratonSessionAmend reducer

const defaultState = {
  name: undefined,
  description: undefined,
  requestedPrefix: undefined,
  sampleId: undefined,
  idRegexPattern: undefined,
  supportingReferences: undefined,
  additionalInformation: undefined,
  institutionRorId: undefined,
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

const prefixRegistrationSessionAmendReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_PREFIXREGISTRATIONSESSIONAMENDFIELD': {
    return { ...state, [action.field]: action.value };
  }

  case 'SET_PREFIXREGISTRATIONSESSIONAMEND': {
    return {
      ...state,
      name: action.registrationSessionAmend.name,
      description: action.registrationSessionAmend.description,
      requestedPrefix: action.registrationSessionAmend.requestedPrefix,
      sampleId: action.registrationSessionAmend.sampleId,
      idRegexPattern: action.registrationSessionAmend.idRegexPattern,
      supportingReferences: action.registrationSessionAmend.supportingReferences,
      additionalInformation: action.registrationSessionAmend.additionalInformation,
      institutionRorId: action.registrationSessionAmend.institutionRorId,
      institutionName: action.registrationSessionAmend.institutionName,
      institutionDescription: action.registrationSessionAmend.institutionDescription,
      institutionHomeUrl: action.registrationSessionAmend.institutionHomeUrl,
      institutionLocation: action.registrationSessionAmend.institutionLocation,
      namespaceEmbeddedInLui: action.registrationSessionAmend.namespaceEmbeddedInLui,
      providerName: action.registrationSessionAmend.providerName,
      providerDescription: action.registrationSessionAmend.providerDescription,
      providerHomeUrl: action.registrationSessionAmend.providerHomeUrl,
      providerCode: action.registrationSessionAmend.providerCode,
      providerUrlPattern: action.registrationSessionAmend.providerUrlPattern,
      providerLocation: action.registrationSessionAmend.providerLocation,
      requesterName: action.registrationSessionAmend.requesterName,
      requesterEmail: action.registrationSessionAmend.requesterEmail
    };
  }

  default:
    return state;
  }
}


export default prefixRegistrationSessionAmendReducer;
