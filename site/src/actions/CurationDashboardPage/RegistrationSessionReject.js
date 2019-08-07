//
// RegistrationSessionReject actions.
//

// Redux store update for registration session new reject.
export const setRegistrationSessionReject = (rejectionReason, additionalInformation, registrationSessionType) => {
    const action = {
      type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONREJECT`,
      rejectionReason,
      additionalInformation
    };

    return action;
  };
