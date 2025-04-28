// Prefix Registration List Params reducer

const defaultState = {
  number: 0,
  size: 5,
  totalElements: 0,
  totalPages: 0
};

const prefixRegistrationSessionListParamsReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_PREFIXREGISTRATIONSESSIONLISTPARAMS':
    return { ...state, ...action.page };

  default:
    return state;
  }
}


export default prefixRegistrationSessionListParamsReducer;
