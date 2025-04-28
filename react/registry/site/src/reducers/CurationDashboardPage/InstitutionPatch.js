// institution patch reducer

const defaultState = {
  id: undefined,
  institution: {
    name: undefined,
    description: undefined,
    location: undefined,
    homeUrl: undefined
  }
};

const institutionPatchReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_INSTITUTIONPATCHREDUCERFIELD': {
    return {
      ...state,
      namespace: {
        ...state.namespace,
        [action.field]: action.value
      }
    };
  }

  case 'SET_INSTITUTIONPATCHREDUCER': {
    return {
      id: action.id,
      namespace: {
        name: action.institution.name,
        description: action.institution.description,
        location: action.institution.location,
        homeUrl: action.institution.homeUrl
      }
    };
  }

  default:
    return state;
  }
}


export default institutionPatchReducer;
