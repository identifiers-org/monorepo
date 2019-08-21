// Institution List reducer

const defaultState = [];

const institutionListReducer = (state = defaultState, action) => {
  switch (action.type) {

  // Set store institution list to the supplied one.
  case 'SET_INSTITUTIONLIST':
    return action.institutionList.sort((a, b) => a.name < b.name ? -1 : a.name > b.name ? 1 : 0)

  case 'SET_INSTITUTION':
    return [...state.filter(institution => institution.id !== action.institution.id), action.institution].sort((a, b) => a.name < b.name ? -1 : a.name > b.name ? 1 : 0);

  default:
    return state;
  }
}


export default institutionListReducer;
