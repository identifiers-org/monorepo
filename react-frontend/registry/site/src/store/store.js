import { createStore, combineReducers, applyMiddleware, compose } from 'redux';
import thunk from 'redux-thunk';

// Reducers.
import authReducer from '../reducers/Auth';

import configReducer from '../reducers/ConfigReducer';

import curationInstitutionListReducer from '../reducers/CurationDashboardPage/CurationInstitutionList';
import curationInstitutionListParamsReducer from '../reducers/CurationDashboardPage/CurationInstitutionListParams';
import institutionListReducer from '../reducers/InstitutionList';
import institutionPatchReducer from '../reducers/CurationDashboardPage/InstitutionPatch';

import locationListReducer from '../reducers/LocationList';

import namespaceListReducer from '../reducers/RegistryBrowser/NamespaceList';
import namespaceListParamsReducer from '../reducers/RegistryBrowser/NamespaceListParams';
import namespacePatchReducer from '../reducers/NamespacePatch';

import prefixRegistrationRequestFieldReducer from '../reducers/PrefixRegistrationRequestField';
import prefixRegistrationSessionListReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSessionList';
import prefixRegistrationSessionListParamsReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSessionListParams';
import prefixRegistrationSessionReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSession';
import prefixRegistrationSessionAcceptReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSessionAccept';
import prefixRegistrationSessionAmendReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSessionAmend';
import prefixRegistrationSessionCommentReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSessionComment';
import prefixRegistrationSessionRejectReducer from '../reducers/CurationDashboardPage/PrefixRegistrationSessionReject';

import resourcePatchReducer from '../reducers/ResourcePatch';

import resourceRegistrationRequestFieldReducer from '../reducers/ResourceRegistrationRequestField';
import resourceRegistrationSessionListReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSessionList';
import resourceRegistrationSessionListParamsReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSessionListParams';
import resourceRegistrationSessionReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSession';
import resourceRegistrationSessionAcceptReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSessionAccept';
import resourceRegistrationSessionAmendReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSessionAmend';
import resourceRegistrationSessionCommentReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSessionComment';
import resourceRegistrationSessionRejectReducer from '../reducers/CurationDashboardPage/ResourceRegistrationSessionReject';

import schemaOrgMetadataReducer from '../reducers/SchemaOrgMetadataReducer';


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

    curationDashboard: combineReducers({
      curationInstitutionList: curationInstitutionListReducer,
      curationInstitutionListParams: curationInstitutionListParamsReducer,
      curationEditInstitution: institutionPatchReducer,

      prefixRegistrationSessionList: prefixRegistrationSessionListReducer,
      prefixRegistrationSessionListParams: prefixRegistrationSessionListParamsReducer,
      prefixRegistrationSession: prefixRegistrationSessionReducer,
      prefixRegistrationSessionAccept: prefixRegistrationSessionAcceptReducer,
      prefixRegistrationSessionAmend: prefixRegistrationSessionAmendReducer,
      prefixRegistrationSessionComment: prefixRegistrationSessionCommentReducer,
      prefixRegistrationSessionReject: prefixRegistrationSessionRejectReducer,

      resourceRegistrationSessionList: resourceRegistrationSessionListReducer,
      resourceRegistrationSessionListParams: resourceRegistrationSessionListParamsReducer,
      resourceRegistrationSession: resourceRegistrationSessionReducer,
      resourceRegistrationSessionAccept: resourceRegistrationSessionAcceptReducer,
      resourceRegistrationSessionAmend: resourceRegistrationSessionAmendReducer,
      resourceRegistrationSessionComment: resourceRegistrationSessionCommentReducer,
      resourceRegistrationSessionReject: resourceRegistrationSessionRejectReducer,

    }),

    curationEditNamespace: namespacePatchReducer,
    curationEditResource: resourcePatchReducer,

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

    resourceRegistrationRequestForm: combineReducers({
      namespacePrefix: resourceRegistrationRequestFieldReducer('namespacePrefix'),

      institutionName: resourceRegistrationRequestFieldReducer('institutionName'),
      institutionDescription: resourceRegistrationRequestFieldReducer('institutionDescription'),
      institutionHomeUrl: resourceRegistrationRequestFieldReducer('institutionHomeUrl'),
      institutionLocation: resourceRegistrationRequestFieldReducer('institutionLocation'),
      institutionIsProvider: resourceRegistrationRequestFieldReducer('institutionIsProvider'),

      providerName: resourceRegistrationRequestFieldReducer('providerName'),
      providerDescription: resourceRegistrationRequestFieldReducer('providerDescription'),
      providerCode: resourceRegistrationRequestFieldReducer('providerCode'),
      providerHomeUrl: resourceRegistrationRequestFieldReducer('providerHomeUrl'),
      providerUrlPattern: resourceRegistrationRequestFieldReducer('providerUrlPattern'),
      sampleId: resourceRegistrationRequestFieldReducer('sampleId'),
      providerLocation: resourceRegistrationRequestFieldReducer('providerLocation'),

      requesterName: resourceRegistrationRequestFieldReducer('requesterName'),
      requesterEmail: resourceRegistrationRequestFieldReducer('requesterEmail')
    }),

    registryBrowser: combineReducers({
      namespaceList: namespaceListReducer,
      namespaceListParams: namespaceListParamsReducer
    }),

    schemaOrgMetadata: schemaOrgMetadataReducer
  }),
  composeEnhancers(applyMiddleware(thunk))
);

export default store;
