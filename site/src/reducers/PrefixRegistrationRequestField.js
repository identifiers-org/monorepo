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
  case `${prefixRegistrationRequestField}/SET_PREFIXREGISTRATIONREQUESTFIELDFIELD`:
    return { ...state, [action.field]: action.value }

  case `${prefixRegistrationRequestField}/SET_PREFIXREGISTRATIONREQUESTFIELDVALIDATION`:
    return { ...state, shouldValidate: action.validation, valid: undefined };

  case `${prefixRegistrationRequestField}/PREFIXREGISTRATIONREQUESTFIELDRESET`:
    return { ...state, value: '', valid: undefined, requestValidate: false, shouldValidate: true };

  default:
    return state;
  }
}


export default prefixRegistrationRequestFieldReducer;
