// PrefixRegistrationRequest reducer.
/* TODO: This has to be used by the prefix registration request form,
 * for validation and UI state purposes. A refactor of the whole form
 * is needed. Will do when we have spare time (TM).
 */

const defaultState = {
  label: '',
  shouldValidate: true,
  requestedValidate: false,
  value: '',
  valid: undefined
};


const prefixRegistrationRequestFieldReducer = (prefixRegistrationRequestField) => (state = defaultState, action) => {
  switch (action.type) {
  case `${prefixRegistrationRequestField}/SET_PREFIXREGISTRATIONFIELDFIELD`:
    return { ...state, [action.field]: action.value }

  // TODO: Phase these out and use the generic setter.
  case `${prefixRegistrationRequestField}/SET_VALUE`:
    return { ...state, value: action.value };

  case `${prefixRegistrationRequestField}/SET_VALIDITY`:
    return { ...state, valid: action.validity };

  case `${prefixRegistrationRequestField}/SET_ERRORMESSAGE`:
    return { ...state, errorMessage: action.errorMessage };

  case `${prefixRegistrationRequestField}/VALIDATE`:
    return { ...state, requestedValidate: true };

  case `${prefixRegistrationRequestField}/VALIDATION_DONE`:
    return { ...state, requestedValidate: false};

  case `${prefixRegistrationRequestField}/SET_VALIDATION`:
    return { ...state, shouldValidate: action.validation, valid: undefined };

  case `${prefixRegistrationRequestField}/SET_LABEL`:
    return { ...state, label: action.label };

  case `${prefixRegistrationRequestField}/RESET`:
    return { ...state, value: '', valid: undefined, requestValidate: false, shouldValidate: true };

  default:
    return state;
  }
}


export default prefixRegistrationRequestFieldReducer;
