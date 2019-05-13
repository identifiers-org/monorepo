// Namespaces List reducer

const defaultState = [];

const prefixRegistrationSessionListReducer = (state = defaultState, action) => {
  switch (action.type) {
  // Set store namespace list to the supplied one.
  case 'SET_PREFIXREGISTRATIONSESSIONLIST':
    return action.prefixRegistrationSessionList;

  default:
    return state;
  }
}


export default prefixRegistrationSessionListReducer;
