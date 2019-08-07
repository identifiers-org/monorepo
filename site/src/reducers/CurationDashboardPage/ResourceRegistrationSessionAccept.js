// ResourceRegistratonSessionAccept reducer

const defaultState = {
  acceptanceReason: undefined,
  additionalInformation: undefined
};

const resourceRegistrationSessionAcceptReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_RESOURCEREGISTRATIONSESSIONACCEPT': {
    return {
      ...state,
      acceptanceReason: action.acceptanceReason,
      additionalInformation: action.additionalInformation
     };
  }

  default:
    return state;
  }
}


export default resourceRegistrationSessionAcceptReducer;
