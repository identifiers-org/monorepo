// Resource Registration Session List reducer

const defaultState = [];

const resourceRegistrationSessionListReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_RESOURCEREGISTRATIONSESSIONLIST':
    return action.registrationSessionList;

  default:
    return state;
  }
}


export default resourceRegistrationSessionListReducer;
