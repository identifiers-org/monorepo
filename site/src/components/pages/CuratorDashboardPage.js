import React from 'react';

import PrefixRegistrationSessionList from '../CuratorDashboardPage/PrefixRegistrationSessionList';
import PageTitle from '../common/PageTitle';


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
                       requesting ammendments or aproving/rejecting it."
        />

        <div className="row">
          <div className="col">
            <PrefixRegistrationSessionList />
          </div>
        </div>
      </>
    );
  }
}

export default CuratorDashboardPage;
