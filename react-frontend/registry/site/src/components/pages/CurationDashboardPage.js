import React from 'react';

// Components.
import CurationInstitutionList from '../CurationDashboardPage/CurationInstitutionList';
import PageTitle from '../common/PageTitle';
import PrefixRegistrationSessionList from '../CurationDashboardPage/PrefixRegistrationSessionList';
import ResourceRegistrationSessionList from '../CurationDashboardPage/ResourceRegistrationSessionList';
import CurationWarningList from '../CurationDashboardPage/CurationWarningList'

import { useMatomo } from '@jonkoops/matomo-tracker-react';

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
          <PrefixRegistrationSessionList />
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
          <ResourceRegistrationSessionList />
        </div>
      </div>

      <PageTitle
        icon="icon-exclamation-triangle"
        title="Curation warnings"
        description="This is a list of warnings that require curator attention. Read their description and act as needed."
      />

      <div className="row mb-5">
        <div className="col">
          <CurationWarningList />
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
          <CurationInstitutionList />
        </div>
      </div>
    </>
  );
}

export default CurationDashboardPage;
