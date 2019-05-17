import { config } from "../config/Config";


// Config reducer

const defaultState = Config;

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
