//
// RegistrationSessionAmend actions.
//

// Redux store update for registration session new amend fields.
export const setRegistrationSessionAmendField = (field, value, registrationSessionType) => {
  const action = {
    type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONAMENDFIELD`,
    field,
    value
  };

  return action;
};


// Redux store update for whole registration session amend.
export const setRegistrationSessionAmend = (registrationSessionAmend, registrationSessionType) => {
  const action = {
    type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONAMEND`,
    registrationSessionAmend
  }

  return action;
};
