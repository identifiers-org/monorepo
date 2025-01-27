// ResourceRegistratonSessionAmend reducer

const defaultState = {
  namespacePrefix: undefined,
  institutionRorId: undefined,
  institutionName: undefined,
  institutionDescription: undefined,
  institutionHomeUrl: undefined,
  institutionLocation: undefined,
  providerName: undefined,
  providerDescription: undefined,
  providerHomeUrl: undefined,
  providerCode: undefined,
  providerUrlPattern: undefined,
  sampleId: undefined,
  providerLocation: undefined,
  requesterName: undefined,
  requesterEmail: undefined
};

const resourceRegistrationSessionAmendReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_RESOURCEREGISTRATIONSESSIONAMENDFIELD': {
    return { ...state, [action.field]: action.value };
  }

  case 'SET_RESOURCEREGISTRATIONSESSIONAMEND': {
    return {
      ...state,
      namespacePrefix: action.registrationSessionAmend.namespacePrefix,
      institutionRorId: action.registrationSessionAmend.institutionRorId,
      institutionName: action.registrationSessionAmend.institutionName,
      institutionDescription: action.registrationSessionAmend.institutionDescription,
      institutionHomeUrl: action.registrationSessionAmend.institutionHomeUrl,
      institutionLocation: action.registrationSessionAmend.institutionLocation,
      providerName: action.registrationSessionAmend.providerName,
      providerDescription: action.registrationSessionAmend.providerDescription,
      providerHomeUrl: action.registrationSessionAmend.providerHomeUrl,
      providerCode: action.registrationSessionAmend.providerCode,
      providerUrlPattern: action.registrationSessionAmend.providerUrlPattern,
      sampleId: action.registrationSessionAmend.sampleId,
      providerLocation: action.registrationSessionAmend.providerLocation,
      requesterName: action.registrationSessionAmend.requesterName,
      requesterEmail: action.registrationSessionAmend.requesterEmail
    };
  }

  default:
    return state;
  }
}


export default resourceRegistrationSessionAmendReducer;
