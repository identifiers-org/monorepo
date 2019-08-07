//
// PrefixRegistrationSessionComment actions.
//

// Redux store update for prefix registration session new comment.
export const setPrefixRegistrationSessionComment = (comment, additionalInformation) => {
    const action = {
      type: 'SET_PREFIXREGISTRATIONSESSIONCOMMENT',
      comment,
      additionalInformation
    };

    return action;
  };
