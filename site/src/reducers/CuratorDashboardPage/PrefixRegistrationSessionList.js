// Prefix Registration Session List reducer

const defaultState = [];

const prefixRegistrationSessionListReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_PREFIXREGISTRATIONSESSIONLIST':
    return action.registrationSessionList;

  default:
    return state;
  }
}


export default prefixRegistrationSessionListReducer;
