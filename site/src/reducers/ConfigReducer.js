import { config } from "../config/Config";


// Config reducer

const defaultState = config;

const configReducer = (state = defaultState, action) => {
  switch (action.type) {
  // Set store namespace list to the supplied one.
  case 'SET_CONFIG':
    return {...state, ...action.config}

  default:
    return state;
  }
}


export default configReducer;
