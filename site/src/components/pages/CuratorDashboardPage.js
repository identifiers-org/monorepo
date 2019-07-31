import React from 'react';

// Components.
import CurationInstitutionList from '../CuratorDashboardPage/CurationInstitutionList';
import PageTitle from '../common/PageTitle';
import PrefixRegistrationSessionList from '../CuratorDashboardPage/PrefixRegistrationSessionList';


class CuratorDashboardPage extends React.Component  {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <>
        <PageTitle
          icon="icon-check-square"
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
}

export default CuratorDashboardPage;
