// PrefixRegistratonSession reducer

const defaultState = {
  closed: false,
  created: undefined,
  id: undefined,
  prefixRegistrationRequest: undefined,
  prefixRegistrationSessionEvents: []
};

const prefixRegistrationSessionReducer = (state = defaultState, action) => {
  switch (action.type) {

  case 'SET_PREFIXREGISTRATIONSESSION':
    return action.prefixRegistrationSession

  default:
    return state;
  }
}


export default prefixRegistrationSessionReducer;
