//
// PrefixRegistrationSessionReject actions.
//

// Redux store update for prefix registration session new reject.
export const setPrefixRegistrationSessionReject = (rejectionReason, additionalInformation) => {
    const action = {
      type: 'SET_PREFIXREGISTRATIONSESSIONREJECT',
      rejectionReason,
      additionalInformation
    };

    return action;
  };
