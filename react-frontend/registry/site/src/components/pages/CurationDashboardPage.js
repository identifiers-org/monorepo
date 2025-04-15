import React from 'react';

// Components.
import PageTitle from '../common/PageTitle';
import PrefixRegistrationSessionList from '../CurationDashboardPage/PrefixRegistrationSessions/PrefixRegistrationSessionList';
import ResourceRegistrationSessionList from '../CurationDashboardPage/ResourceRegistrationSessions/ResourceRegistrationSessionList';
import CurationWarningList from '../CurationDashboardPage/CurationWarnings/CurationWarningList'
import CurationInstitutionList from '../CurationDashboardPage/CurationInstitutionList';

import { useMatomo } from '@jonkoops/matomo-tracker-react';
import CurationWarningListInstructions from "../CurationDashboardPage/CurationWarnings/CurationWarningListInstructions";

const CurationDashboardPage = () => {
  const { trackPageView } = useMatomo();
  trackPageView();

  return (
      <>
        <PageTitle
            icon="icon-leaf"
            title="Prefix Registration Requests"
            description="This is the list of Prefix Registration Requests sent by resource administrators.
                     Clicking on one will expand the information and allow to perform actions like
                     requesting amendments or aproving/rejecting it."
        />

        <div className="row mb-5">
          <div className="col">
            <PrefixRegistrationSessionList/>
          </div>
        </div>

        <PageTitle
            icon="icon-cube"
            title="Resource Registration Requests"
            description="This is the list of Resource Registration Requests sent by resource administrators.
                     Clicking one will expand information and actions like in the one above."
        />

        <div className="row mb-5">
          <div className="col">
            <ResourceRegistrationSessionList/>
          </div>
        </div>

        <PageTitle
            icon="icon-exclamation-triangle"
            title="Summary of open curation warnings"
            description=<>
                This table summarizes warnings that require curator attention.
                <button type="button" data-bs-toggle="modal" className="btn btn-link d-inline px-1 py-0 m-0"
                        data-bs-target="#curation-warning-instructions-modal">
                  Click here for more information.
                </button>
              </>
        />

        <div className="row mb-5">
          <div className="col">
            <CurationWarningList/>

            <div className="modal" id="curation-warning-instructions-modal" tabIndex="-1">
              <div className="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
                  <CurationWarningListInstructions />
              </div>
            </div>
          </div>
        </div>

        <PageTitle
            id="curation-institution"
            icon="icon-sitemap"
            title="Institutions"
            description="This is the list of Institutions currently stored in the registry. Clicking on one
                     will show the detail fields and allow modifications or deletion."
        />

        <div className="row">
          <div className="col">
            <CurationInstitutionList/>
          </div>
        </div>
      </>
  );
}

export default CurationDashboardPage;
