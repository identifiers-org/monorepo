import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';

// Reducers.
import configReducer from '../reducers/ConfigReducer';
import namespaceListReducer from '../reducers/NamespaceList';
import resolvedResourcesReducer from '../reducers/ResolvedResources';
import schemaOrgMetadataReducer from '../reducers/SchemaOrgMetadataReducer';


const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


//
// Store management.
//

// Store creation.
const store = createStore(
  combineReducers({
    config: configReducer,
    namespaceList: namespaceListReducer,
    resolvedResources: resolvedResourcesReducer,
    schemaOrgMetadata: schemaOrgMetadataReducer
  }),
  composeEnhancers(applyMiddleware(thunk))
);

export default store;
