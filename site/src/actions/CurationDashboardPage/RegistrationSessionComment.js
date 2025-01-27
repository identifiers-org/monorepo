//
// RegistrationSessionComment actions.
//

// Redux store update for registration session new comment.
export const setRegistrationSessionComment = (comment, additionalInformation, registrationSessionType) => {
    const action = {
      type: `SET_${registrationSessionType.toUpperCase()}REGISTRATIONSESSIONCOMMENT`,
      comment,
      additionalInformation
    };

    return action;
  };
