// ResolvedResources reducer

const defaultState = [];

const resolvedResourcesReducer = (state = defaultState, action) => {
  switch (action.type) {
    // Set store namespace list to the supplied one.
    case 'SET_RESOLVEDRESOURCES':
      return action.resolvedResources;

    default:
      return state;
  }
}


export default resolvedResourcesReducer;
