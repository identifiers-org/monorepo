// Namespaces List reducer

const defaultState = [];

const curationInstitutionListReducer = (state = defaultState, action) => {
  switch (action.type) {
  // Set store namespace list to the supplied one.
  case 'SET_CURATIONINSTITUTIONLIST':
    return action.curationInstitutionList;

  default:
    return state;
  }
}


export default curationInstitutionListReducer;
