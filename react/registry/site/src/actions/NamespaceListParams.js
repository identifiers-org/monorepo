//
// namespaceListParams actions.
//

// Redux store update for namespace page data.
export const setNamespaceListParams = (namespaceListParams) => {
  return {
    type: 'SET_NAMESPACELISTPARAMS',
    namespaceListParams
  }
};
