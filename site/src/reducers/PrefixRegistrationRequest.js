// PrefixRegistrationRequest reducer.
/* TODO: This has to be used by the prefix registration request form,
 * for validation and UI state purposes. A refactor of the whole form
 * is needed. Will do when we have spare time (TM).
 */

const defaultState = {
  label: '',
  shouldValidate: true,
  valid: undefined,
  value: '',
  validationUrl: undefined,
  validationPayload: {}
};


const prefixRegistrationRequestReducer = (state = defaultState, action) => {
  switch (action.type) {

  case 'SET_PREFIXREGISTRATIONREQUESTFIELD':
    return { ...state, ...action.prefixRegistrationRequest };

  default:
    return state;
  }
}


export default prefixRegistrationRequestReducer;
