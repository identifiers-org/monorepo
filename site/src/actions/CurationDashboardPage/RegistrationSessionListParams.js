//
// RegistrationSessionListParms actions.
//

// Redux store update for registration session list page params.
export const setRegistrationSessionListParams = (page, registrationSessionType) => {
  return {
    type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONLISTPARAMS`,
    page
  };
};
