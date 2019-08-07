// Namespaces List reducer

const defaultState = {
  nameContent: '',
  number: 0,
  size: 20,
  totalElements: 0,
  totalPages: 0
};

const curationInstitutionListParamsReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_CURATIONINSTITUTIONLISTPARAMS':
    return { ...state, ...action.params };

  default:
    return state;
  }
}


export default curationInstitutionListParamsReducer;
