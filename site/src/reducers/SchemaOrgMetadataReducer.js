const defaultState = {};

const schemaOrgMetadataReducer = (state = defaultState, action) => {
  switch (action.type) {
    // Set Schema.org metadata to the supplied one.
    case 'SET_SCHEMAORGMETADATA':
      return {...action.schemaOrgMetadata}

    default:
      return state;
  }
}


export default schemaOrgMetadataReducer;
