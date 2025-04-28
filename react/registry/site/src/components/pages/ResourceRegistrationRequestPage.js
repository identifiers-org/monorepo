import React, {useEffect} from 'react';
import { useMatomo } from '@jonkoops/matomo-tracker-react';

import PageTitle from '../common/PageTitle';
import ResourceRequestForm from '../RequestPages/ResourceRequestForm';

export default () => {
  const { trackPageView } = useMatomo()

  if (process.env.NODE_ENV !== 'development') {
    useEffect(() => {
      trackPageView();
    })
  }

  return (
    <>
      <PageTitle
        icon="icon-list"
        title="Request resource form"
        description={"Please complete this form to register a new resource, in an existing namespace, that can be recognized by the meta-resolvers at identifiers.org and n2t.net. Completing all fields will enable a swift processing of your request."}
      />

      <div className="container py-3">
        <div className="row">
          <div className="mx-auto col-sm-12 col-lg-10">
            <ResourceRequestForm />
          </div>
        </div>
      </div>
    </>
  )
}
