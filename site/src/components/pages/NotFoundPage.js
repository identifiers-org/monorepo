import React from 'react';
import { Link } from 'react-router-dom';

import { useMatomo } from '@datapunt/matomo-tracker-react';


const NotFoundPage = () => {
  const { trackPageView } = this.props.matomo
  trackPageView();
  return (
    <div>
      404 - <Link to="/">Go home</Link>
    </div>
  )
};


export default NotFoundPage;
