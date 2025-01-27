//
// RegistrationSessionAccept actions.
//

// Redux store update for prefix registration session new accept.
export const setRegistrationSessionAccept = (acceptanceReason, additionalInformation, registrationSessionType) => {
    const action = {
      type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONACCEPT`,
      acceptanceReason,
      additionalInformation
    };

    return action;
  };
