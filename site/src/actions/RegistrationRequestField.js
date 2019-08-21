//
// registrationRequestField actions.
//


// Redux store update for registration request field.
export const setRegistrationRequestFieldField = (registrationType, id, field, value) => ({
  type: `${id}/SET_${registrationType}REGISTRATIONREQUESTFIELDFIELD`,
  field,
  value
});

export const setValidation = (registrationType, id, validation) => ({
  type: `${id}/SET_${registrationType}REGISTRATIONREQUESTFIELDVALIDATION`,
  validation
});

export const resetValidityStatus = (registrationType, id) => ({
  type: `${id}/RESET_${registrationType}REGISTRATIONREQUESTFIELDVALIDITYSTATUS`
})

export const reset = (registrationType, id) => ({
  type: `${id}/${registrationType}REGISTRATIONREQUESTFIELDRESET`
});
