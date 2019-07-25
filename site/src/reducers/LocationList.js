// Location List reducer

const defaultState = [];

const locationListReducer = (state = defaultState, action) => {
  switch (action.type) {

  // Set store location list to the supplied one.
  case 'SET_LOCATIONLIST':
    return action.locationList

  default:
    return state;
  }
}


export default locationListReducer;
