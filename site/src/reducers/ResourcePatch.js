// Resource patch reducer

const defaultState = {
  id: undefined,
  resource: {
    providerCode: undefined,
    description: undefined,
    urlPattern: undefined,
    institution: undefined,
    resourceHomeUrl: undefined,
    location: undefined,
    sampleId: undefined,
    official: undefined
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
        providerCode: action.resource.providerCode,
        description: action.resource.description,
        urlPattern: action.resource.urlPattern,
        institution: action.resource.institution,
        resourceHomeUrl: action.resource.resourceHomeUrl,
        location: action.resource.location,
        sampleId: action.resource.sampleId,
        official: action.resource.official
      }
    };
  }

  default:
    return state;
  }
}


export default resourcePatchReducer;
