import React from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCheck, faTimes, faComment, faEdit, faFile } from '@fortawesome/free-solid-svg-icons';

import PrefixRegistrationSessionEventStart from './PrefixRegistrationSessionEventStart';
import PrefixRegistrationSessionEventAmend from './PrefixRegistrationSessionEventAmend';
import PrefixRegistrationSessionEventComment from './PrefixRegistrationSessionEventComment';
import PrefixRegistrationSessionEventAccept from './PrefixRegistrationSessionEventAccept';
import PrefixRegistrationSessionEventReject from './PrefixRegistrationSessionEventReject';

import Moment from 'moment';


const PrefixRegistrationSessionEvent = ({ data }) => (
  <>
    {
      data.eventName === 'START' &&
        <div className="callout callout-primary">
          <div className="mb-3 pl-3 bg-dark text-white rounded-sm">
            <FontAwesomeIcon icon={faFile} />&nbsp;
            <strong>Original request submission at {Moment(data.created).format('llll')}</strong>
          </div>
          <PrefixRegistrationSessionEventStart data={data} />
        </div>
    } {
      data.eventName === 'AMEND' &&
        <div className="callout callout-warning">
          <div className="mb-3 pl-3 bg-dark text-white rounded-sm">
            <FontAwesomeIcon icon={faEdit} />&nbsp;
            <strong>Request amend at {Moment(data.created).format('llll')}</strong>
          </div>
          <PrefixRegistrationSessionEventAmend data={data} />
        </div>
    } {
      data.eventName === 'COMMENT' &&
        <div className="callout callout-secondary">
          <div className="mb-3 pl-3 bg-dark text-white rounded-sm">
            <FontAwesomeIcon icon={faComment} />&nbsp;
            <strong>Request comment at {Moment(data.created).format('llll')}</strong>
          </div>
          <PrefixRegistrationSessionEventComment data={data} />
        </div>
    } {
      data.eventName === 'ACCEPT' &&
        <div className="callout callout-success">
          <div className="mb-3 pl-3 bg-dark text-white rounded-sm">
            <FontAwesomeIcon icon={faCheck} />&nbsp;
            <strong>Request accept at {Moment(data.created).format('llll')}</strong>
          </div>
          <PrefixRegistrationSessionEventAccept data={data} />
        </div>
    } {
      data.eventName === 'REJECT' &&
        <div className="callout callout-danger">
          <div className="mb-3 pl-3 bg-dark text-white rounded-sm">
            <FontAwesomeIcon icon={faTimes} />&nbsp;
            <strong>Request reject at {Moment(data.created).format('llll')}</strong>
          </div>
          <PrefixRegistrationSessionEventReject data={data} />
        </div>
    }
  </>
);


export default PrefixRegistrationSessionEvent;
