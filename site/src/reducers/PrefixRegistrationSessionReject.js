// PrefixRegistratonSessionReject reducer

const defaultState = {
  rejectionReason: undefined,
  additionalInformation: undefined
};

const PrefixRegistrationSessionRejectReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_PREFIXREGISTRATIONSESSIONREJECT': {
    return {
      ...state,
      rejectionReason: action.rejectionReason,
      additionalInformation: action.additionalInformation
     };
  }

  default:
    return state;
  }
}


export default PrefixRegistrationSessionRejectReducer;
