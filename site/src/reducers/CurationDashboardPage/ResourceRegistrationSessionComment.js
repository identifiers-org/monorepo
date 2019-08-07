// ResourceRegistratonSessionComment reducer

const defaultState = {
  comment: undefined,
  additionalInformation: undefined
};

const resourceRegistrationSessionCommentReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_RESOURCEREGISTRATIONSESSIONCOMMENT': {
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


export default resourceRegistrationSessionCommentReducer;
