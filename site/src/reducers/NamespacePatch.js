// namespace patch reducer

const defaultState = {
  id: undefined,
  namespace: {
    deprecated: undefined,
    deprecationDate: undefined,
    description: undefined,
    mirId: undefined,
    modified: undefined,
    name: undefined,
    namespaceEmbeddedInLui: undefined,
    pattern: undefined,
    prefix: undefined,
    sampleId: undefined
  }
};

const namespacePatchReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_NAMESPACEPATCHREDUCERFIELD': {
    return {
      ...state,
      namespace: {
        ...state.namespace,
        [action.field]: action.value
      }
    };
  }

  case 'SET_NAMESPACEPATCHREDUCER': {
    return {
      id: action.id,
      namespace: {
        deprecated: action.namespace.deprecated,
        deprecationDate: action.namespace.deprecationDate,
        description: action.namespace.description,
        mirId: action.namespace.mirId,
        modified: action.namespace.modified,
        name: action.namespace.name,
        namespaceEmbeddedInLui: action.namespace.namespaceEmbeddedInLui,
        pattern: action.namespace.pattern,
        prefix: action.namespace.prefix,
        sampleId: action.namespace.sampleId
      }
    };
  }

  default:
    return state;
  }
}


export default namespacePatchReducer;
