// Namespaces List reducer

const defaultState = [];

const namespaceListReducer = (state = defaultState, action) => {
  switch (action.type) {
  // Set store namespace list to the supplied one.
    case 'SET_STATISTICS': {
      return state.map(ns => {
        if (ns.prefix == action.prefix) {
          ns['stats'] = action.stats;
        }
        return ns;
      });
    }

  case 'SET_NAMESPACELIST':
    return action.namespaceList;

  case 'SET_RESOURCES': {
    // Adds resources to namespace specified by action.prefix.
    let newNamespaceList = state.map(ns => {
      if (ns.prefix === action.prefix) {
        ns['resources'] = action.resources;
      }

      return ns;
    });

    return newNamespaceList;
  }

  default:
    return state;
  }
}


export default namespaceListReducer;
