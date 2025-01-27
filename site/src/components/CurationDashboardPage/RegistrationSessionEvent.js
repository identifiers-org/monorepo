import React from 'react';

import RegistrationSessionEventStart from './RegistrationSessionEventStart';
import RegistrationSessionEventAmend from './RegistrationSessionEventAmend';
import RegistrationSessionEventComment from './PrefixRegistrationSessionEventComment';
import RegistrationSessionEventAccept from './RegistrationSessionEventAccept';
import RegistrationSessionEventReject from './RegistrationSessionEventReject';

import Moment from 'moment';


const RegistrationSessionEvent = ({ data, registrationSessionType }) => (
  <>
    {
      data.eventName === 'START' &&
        <div className="callout callout-primary">
          <div className="bg-dark text-white rounded-sm d-flex justify-content-between mb-3 px-3 ">
            <div>
              {/* TODO: change to file when EBI adds it to EBI-Font-icons */}
              <i className="icon icon-common icon-search-document mr-1" />
              <strong>Original request submission at {Moment(data.created).format('llll')}</strong>
            </div>
            <div>
              <strong><i className="icon icon-common icon-user mr-1" />{data.actor}</strong>
            </div>
          </div>
          <RegistrationSessionEventStart data={data} registrationSessionType={registrationSessionType} />
        </div>
    } {
      data.eventName === 'AMEND' &&
        <div className="callout callout-warning">
          <div className="bg-dark text-white rounded-sm d-flex justify-content-between mb-3 px-3 ">
            <div>
              <i className="icon icon-common icon-edit" />&nbsp;
              <strong>Request amend at {Moment(data.created).format('llll')}</strong>
            </div>
            <div>
              <strong><i className="icon icon-common icon-user mr-1" />{data.actor}</strong>
            </div>
          </div>
          <RegistrationSessionEventAmend data={data} registrationSessionType={registrationSessionType} />
        </div>
    } {
      data.eventName === 'COMMENT' &&
        <div className="callout callout-secondary">
          <div className="bg-dark text-white rounded-sm d-flex justify-content-between mb-3 px-3 ">
            <div>
              <i className="icon icon-common icon-comment" />&nbsp;
              <strong>Request comment at {Moment(data.created).format('llll')}</strong>
            </div>
            <div>
              <strong><i className="icon icon-common icon-user mr-1" />{data.actor}</strong>
            </div>
          </div>
          <RegistrationSessionEventComment data={data} registrationSessionType={registrationSessionType} />
        </div>
    } {
      data.eventName === 'ACCEPT' &&
        <div className="callout callout-success">
          <div className="bg-dark text-white rounded-sm d-flex justify-content-between mb-3 px-3 ">
            <div>
              <i className="icon icon-common icon-check" />&nbsp;
              <strong>Request accept at {Moment(data.created).format('llll')}</strong>
            </div>
            <div>
              <strong><i className="icon icon-common icon-user mr-1" />{data.actor}</strong>
            </div>
          </div>
          <RegistrationSessionEventAccept data={data} registrationSessionType={registrationSessionType} />
        </div>
    } {
      data.eventName === 'REJECT' &&
        <div className="callout callout-danger">
          <div className="bg-dark text-white rounded-sm d-flex justify-content-between mb-3 px-3 ">
            <div>
              <i className="icon icon-common icon-times" />&nbsp;
              <strong>Request reject at {Moment(data.created).format('llll')}</strong>
            </div>
            <div>
              <strong><i className="icon icon-common icon-user mr-1" />{data.actor}</strong>
            </div>
          </div>
          <RegistrationSessionEventReject data={data} registrationSessionType={registrationSessionType} />
        </div>
    }
  </>
);


export default RegistrationSessionEvent;
