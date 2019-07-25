// Institution List reducer

const defaultState = [];

const institutionListReducer = (state = defaultState, action) => {
  switch (action.type) {

  // Set store institution list to the supplied one.
  case 'SET_INSTITUTIONLIST':
    return action.institutionList

  default:
    return state;
  }
}


export default institutionListReducer;
