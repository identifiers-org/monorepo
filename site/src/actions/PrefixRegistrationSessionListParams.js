//
// PrefixRegistrationSessionListParms actions.
//

// Redux store update for prefix registration session list page params.
export const setPrefixRegistrationSessionListParams = (page) => {
  return {
    type: 'SET_PREFIXREGISTRATIONSESSIONLISTPARAMS',
    page
  };
};
