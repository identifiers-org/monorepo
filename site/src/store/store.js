import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';

import configReducer from '../reducers/ConfigReducer';
import namespaceListReducer from '../reducers/NamespaceList';
import namespaceListParamsReducer from '../reducers/NamespaceListParams';
import prefixRegistrationRequestFieldReducer from '../reducers/PrefixRegistrationRequestField';
import prefixRegistrationSessionListReducer from '../reducers/PrefixRegistrationSessionList';
import prefixRegistrationSessionListParamsReducer from '../reducers/PrefixRegistrationSessionListParams';
import PrefixRegistrationSessionReducer from '../reducers/PrefixRegistrationSession';
import PrefixRegistrationSessionAcceptReducer from '../reducers/PrefixRegistrationSessionAccept';
import PrefixRegistrationSessionAmendReducer from '../reducers/PrefixRegistrationSessionAmend';
import PrefixRegistrationSessionCommentReducer from '../reducers/PrefixRegistrationSessionComment';
import PrefixRegistrationSessionRejectReducer from '../reducers/PrefixRegistrationSessionReject';
import locationListReducer from '../reducers/LocationList';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


//
// Store management.
//

// Store creation.
const store = createStore(
  combineReducers({
    config: configReducer,
    registryBrowser: combineReducers({
      namespaceList: namespaceListReducer,
      namespaceListParams: namespaceListParamsReducer
    }),
    prefixRegistrationRequestForm: combineReducers({
      name: prefixRegistrationRequestFieldReducer('name'),
      description: prefixRegistrationRequestFieldReducer('description'),
      requestedPrefix: prefixRegistrationRequestFieldReducer('requestedPrefix'),
      sampleId: prefixRegistrationRequestFieldReducer('sampleId'),
      idRegexPattern: prefixRegistrationRequestFieldReducer('idRegexPattern'),
      supportingReferences: prefixRegistrationRequestFieldReducer('supportingReferences'),
      additionalInformation: prefixRegistrationRequestFieldReducer('additionalInformation'),
      institutionName: prefixRegistrationRequestFieldReducer('institutionName'),
      institutionDescription: prefixRegistrationRequestFieldReducer('institutionDescription'),
      institutionHomeUrl: prefixRegistrationRequestFieldReducer('institutionHomeUrl'),
      institutionLocation: prefixRegistrationRequestFieldReducer('institutionLocation'),
      institutionIsProvider: prefixRegistrationRequestFieldReducer('institutionIsProvider'),
      providerName: prefixRegistrationRequestFieldReducer('providerName'),
      providerDescription: prefixRegistrationRequestFieldReducer('providerDescription'),
      providerCode: prefixRegistrationRequestFieldReducer('providerCode'),
      providerHomeUrl: prefixRegistrationRequestFieldReducer('providerHomeUrl'),
      providerUrlPattern: prefixRegistrationRequestFieldReducer('providerUrlPattern'),
      providerLocation: prefixRegistrationRequestFieldReducer('providerLocation'),
      requesterName: prefixRegistrationRequestFieldReducer('requesterName'),
      requesterEmail: prefixRegistrationRequestFieldReducer('requesterEmail')
    }),
    curatorDashboard: combineReducers({
      prefixRegistrationSessionList: prefixRegistrationSessionListReducer,
      prefixRegistrationSessionListParams: prefixRegistrationSessionListParamsReducer,
      prefixRegistrationSession: PrefixRegistrationSessionReducer,
      prefixRegistrationSessionAccept: PrefixRegistrationSessionAcceptReducer,
      prefixRegistrationSessionAmend: PrefixRegistrationSessionAmendReducer,
      prefixRegistrationSessionComment: PrefixRegistrationSessionCommentReducer,
      prefixRegistrationSessionReject: PrefixRegistrationSessionRejectReducer
    }),
    locationList: locationListReducer
  }),
  composeEnhancers(applyMiddleware(thunk))
);

export default store;
