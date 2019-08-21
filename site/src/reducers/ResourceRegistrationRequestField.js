// ResourceRegistrationRequest reducer.
/* TODO: This has to be used by the resource registration request form,
 * for validation and UI state purposes. A refactor of the whole form
 * is needed. Will do when we have spare time (TM).
 */

const defaultState = {
  errorMessage: undefined,
  label: '',
  shouldValidate: true,
  requestedValidate: false,
  value: '',
  valid: undefined
};


const resourceRegistrationRequestFieldReducer = (resourceRegistrationRequestField) => (state = defaultState, action) => {
  switch (action.type) {
  case `${resourceRegistrationRequestField}/SET_RESOURCEREGISTRATIONREQUESTFIELDFIELD`:
    return { ...state, [action.field]: action.value }

  case `${resourceRegistrationRequestField}/SET_RESOURCEREGISTRATIONREQUESTFIELDVALIDATION`:
    return { ...state, shouldValidate: action.validation, valid: undefined };

  case `${resourceRegistrationRequestField}/RESOURCEREGISTRATIONREQUESTFIELDRESET`:
    return { ...state, value: '', valid: undefined, requestValidate: false, shouldValidate: true };

  default:
    return state;
  }
}


export default resourceRegistrationRequestFieldReducer;
