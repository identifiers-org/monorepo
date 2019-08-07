//
// PrefixRegistrationSessionAccept actions.
//

// Redux store update for prefix registration session new accept.
export const setPrefixRegistrationSessionAccept = (acceptanceReason, additionalInformation) => {
    const action = {
      type: 'SET_PREFIXREGISTRATIONSESSIONACCEPT',
      acceptanceReason,
      additionalInformation
    };

    return action;
  };
