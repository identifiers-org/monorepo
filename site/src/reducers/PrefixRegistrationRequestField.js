// PrefixRequestField reducer

const defaultState = {
  value: '',
  valid: undefined,
  requestedValidate: false,
  shouldValidate: true,
  label: ''
};


const prefixRequestFieldReducer = (id) => (state = defaultState, action) => {
  switch (action.type) {
  case `${id}/SET_VALUE`:
    return { ...state, value: action.value };

  case `${id}/SET_VALIDITY`:
    return { ...state, valid: action.validity };

  case `${id}/SET_ERRORMESSAGE`:
    return { ...state, errorMessage: action.errorMessage };

  case `${id}/VALIDATE`:
    return { ...state, requestedValidate: true };

  case `${id}/VALIDATION_DONE`:
    return { ...state, requestedValidate: false};

  case `${id}/SET_VALIDATION`:
    return { ...state, shouldValidate: action.validation, valid: undefined };

  case `${id}/SET_LABEL`:
    return { ...state, label: action.label };
  
  case `${id}/RESET`:
    return { ...state, value: '', valid: undefined, requestValidate: false, shouldValidate: true };
    
  default:
    return state;
  }
}


export default prefixRequestFieldReducer;
