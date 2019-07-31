// Resource patch reducer

const defaultState = {
  id: undefined,
  resource: {
    mirId: undefined,
    urlPattern: undefined,
    name: undefined,
    description: undefined,
    official: undefined,
    providerCode: undefined,
    sampleId: undefined,
    resourceHomeUrl: undefined
  }
};

const resourcePatchReducer = (state = defaultState, action) => {
  switch (action.type) {
  case 'SET_RESOURCEPATCHREDUCERFIELD': {
    return {
      ...state,
      resource: {
        ...state.resource,
        [action.field]: action.value
      }
    };
  }

  case 'SET_RESOURCEPATCHREDUCER': {
    return {
      id: action.id,
      resource: {
        mirId: action.resource.mirId,
        urlPattern: action.resource.urlPattern,
        name: action.resource.name,
        description: action.resource.description,
        official: action.resource.official,
        providerCode: action.resource.providerCode,
        sampleId: action.resource.sampleId,
        resourceHomeUrl: action.resource.resourceHomeUrl
      }
    };
  }

  default:
    return state;
  }
}


export default resourcePatchReducer;
