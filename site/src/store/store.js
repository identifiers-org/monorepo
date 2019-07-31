import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';

// Reducers.
import authReducer from '../reducers/Auth';
import configReducer from '../reducers/ConfigReducer';
import curationInstitutionListReducer from '../reducers/CuratorDashboardPage/CurationInstitutionList';
import curationInstitutionListParamsReducer from '../reducers/CuratorDashboardPage/CurationInstitutionListParams';
import institutionListReducer from '../reducers/InstitutionList';
import locationListReducer from '../reducers/LocationList';
import namespaceListReducer from '../reducers/RegistryBrowser/NamespaceList';
import namespaceListParamsReducer from '../reducers/RegistryBrowser/NamespaceListParams';
import namespacePatchReducer from '../reducers/NamespacePatch';
import prefixRegistrationRequestFieldReducer from '../reducers/PrefixRegistrationRequestField';
import prefixRegistrationSessionListReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSessionList';
import prefixRegistrationSessionListParamsReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSessionListParams';
import prefixRegistrationSessionReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSession';
import prefixRegistrationSessionAcceptReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSessionAccept';
import prefixRegistrationSessionAmendReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSessionAmend';
import prefixRegistrationSessionCommentReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSessionComment';
import prefixRegistrationSessionRejectReducer from '../reducers/CuratorDashboardPage/PrefixRegistrationSessionReject';
import resourcePatchReducer from '../reducers/ResourcePatch';

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
      curationInstitutionList: curationInstitutionListReducer,
      curationInstitutionListParams: curationInstitutionListParamsReducer,
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
    institutionList: institutionListReducer,
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
