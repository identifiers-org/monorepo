// ResourceRegistratonSession reducer

const defaultState = {
  closed: false,
  created: undefined,
  id: undefined,
  resourceRegistrationRequest: undefined,
  resourceRegistrationSessionEvents: []
};

const resourceRegistrationSessionReducer = (state = defaultState, action) => {
  switch (action.type) {

  case 'SET_RESOURCEREGISTRATIONSESSION':
    return action.resourceRegistrationSession

  default:
    return state;
  }
}


export default resourceRegistrationSessionReducer;
