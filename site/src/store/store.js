import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';

import namespaceListReducer from '../reducers/NamespaceList';
import configReducer from '../reducers/ConfigReducer';


const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


//
// Store management.
//

// Store creation.
const store = createStore(
  combineReducers({
    namespaceList: namespaceListReducer,
    config: configReducer
  }),
  composeEnhancers(applyMiddleware(thunk))
);

export default store;
