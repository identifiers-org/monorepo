import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';

// Reducers.
import authReducer from '../reducers/Auth';
import configReducer from '../reducers/ConfigReducer';
import locationListReducer from '../reducers/LocationList';
import namespaceListReducer from '../reducers/NamespaceList';
import namespaceListParamsReducer from '../reducers/NamespaceListParams';
import namespacePatchReducer from '../reducers/NamespacePatchReducer';
import prefixRegistrationRequestFieldReducer from '../reducers/PrefixRegistrationRequestField';
import prefixRegistrationSessionListReducer from '../reducers/PrefixRegistrationSessionList';
import prefixRegistrationSessionListParamsReducer from '../reducers/PrefixRegistrationSessionListParams';
import prefixRegistrationSessionReducer from '../reducers/PrefixRegistrationSession';
import prefixRegistrationSessionAcceptReducer from '../reducers/PrefixRegistrationSessionAccept';
import prefixRegistrationSessionAmendReducer from '../reducers/PrefixRegistrationSessionAmend';
import prefixRegistrationSessionCommentReducer from '../reducers/PrefixRegistrationSessionComment';
import prefixRegistrationSessionRejectReducer from '../reducers/PrefixRegistrationSessionReject';
import resourcePatchReducer from '../reducers/ResourcePatchReducer';

// Middlewares.
// TODO: Convert validator to middleware.
// import fieldValidationMiddleware from '../middleware/fieldValidationMiddleware';

const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;


//
// Store management.
//

// Store creation.
const store = createStore(
  combineReducers({
    auth: authReducer,
    config: configReducer,
    curatorDashboard: combineReducers({
      prefixRegistrationSessionList: prefixRegistrationSessionListReducer,
      prefixRegistrationSessionListParams: prefixRegistrationSessionListParamsReducer,
      prefixRegistrationSession: prefixRegistrationSessionReducer,
      prefixRegistrationSessionAccept: prefixRegistrationSessionAcceptReducer,
      prefixRegistrationSessionAmend: prefixRegistrationSessionAmendReducer,
      prefixRegistrationSessionComment: prefixRegistrationSessionCommentReducer,
      prefixRegistrationSessionReject: prefixRegistrationSessionRejectReducer
    }),
    curatorEditNamespace: namespacePatchReducer,
    curatorEditResource: resourcePatchReducer,
    locationList: locationListReducer,
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
    registryBrowser: combineReducers({
      namespaceList: namespaceListReducer,
      namespaceListParams: namespaceListParamsReducer
    }),
  }),
  composeEnhancers(applyMiddleware(thunk))
);

export default store;
