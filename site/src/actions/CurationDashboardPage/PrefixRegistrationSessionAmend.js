//
// PrefixRegistrationSessionAmend actions.
//

// Redux store update for prefix registration session new amend fields.
export const setPrefixRegistrationSessionAmendField = (field, value) => {
  const action = {
    type: 'SET_PREFIXREGISTRATIONSESSIONAMENDFIELD',
    field,
    value
  };

  return action;
};


// Redux store update for whole session amend.
export const setPrefixRegistrationSessionAmend = (prefixRegistrationSessionAmend) => {
  const action = {
    type: 'SET_PREFIXREGISTRATIONSESSIONAMEND',
    prefixRegistrationSessionAmend
  }

  return action;
};
