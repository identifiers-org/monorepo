// PrefixRegistratonSessionComment reducer

const defaultState = {
  comment: undefined,
  additionalInformation: undefined
};

const prefixRegistrationSessionCommentReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_PREFIXREGISTRATIONSESSIONCOMMENT': {
    return {
      ...state,
      comment: action.comment,
      additionalInformation: action.additionalInformation
     };
  }

  default:
    return state;
  }
}


export default prefixRegistrationSessionCommentReducer;
