//
// prefixRegistrationRequestField actions.
//


// Redux store update for prefix request field.
export const setValue = (id, value) => {
  return {
    type: `${id}/SET_VALUE`,
    value
  };
};

export const setValidity = (id, validity) => {
  return {
    type: `${id}/SET_VALIDITY`,
    validity
  }
}

export const setErrorMessage = (id, errorMessage) => {
  return {
    type: `${id}/SET_ERRORMESSAGE`,
    errorMessage
  }
}

export const setValidation = (id, validation) => {
  return {
    type: `${id}/SET_VALIDATION`,
    validation
  }
}

export const requestValidation = (id) => {
  return {
    type: `${id}/VALIDATE`
  }
}

export const validationDone = (id) => {
  return {
    type: `${id}/VALIDATION_DONE`
  }
}

export const setLabel = (id, label) => {
  return {
    type: `${id}/SET_LABEL`,
    label
  }
}

export const reset = (id) => {
  return {
    type: `${id}/RESET`
  }
}
