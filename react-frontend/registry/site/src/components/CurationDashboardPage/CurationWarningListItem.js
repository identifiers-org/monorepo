import React, {useEffect, useState} from "react";
import Spinner from "../common/Spinner";
import CurationWarningEventList from "./CurationWarningEventList";
import CurationWarningDetails from "./CurationWarningDetails";
import dateTimeFormat from "../../utils/dateTimeFormat";

const targetTypes = {
  institution: Symbol("Institution"),
  resource: Symbol("Resource"),
  namespace: Symbol("Namespace")
}

const getTypeForTarget = (target) => {
  if (target === null) return null;

  if (target?.hasOwnProperty("resourceHomeUrl")) {
    return targetTypes.resource;
  } else if (target?.hasOwnProperty("prefix")) {
    return targetTypes.namespace;
  } else if (!target?.hasOwnProperty("mirId")) {
    return targetTypes.institution;
  } else {
    return null;
  }
}

export default ({warning}) => {
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);

  const [target, setTarget] = useState(null);

  useEffect(() => {
    if (loading === null) {
      const targetUrl = warning?._links?.target.href;
      if (targetUrl) {
        setLoading(true);
        fetch(targetUrl)
            .then(response => response.json())
            .then(json => setTarget(json))
            .catch(() => setFailed(true))
            .finally(() => setLoading(false));
      }
    }
  }, [
    setLoading, setTarget, warning, setFailed
  ])

  if (loading) {
    return <div className="card mb-1">
      <div className="card-header py-1 pr-1"><Spinner compact noText noCenter/></div>
    </div>;
  }
  if (failed) {
    return <div className="card mb-1">
      <div className="card-header py-1 pr-1"> Failed get information for target of notification!</div>
    </div>;
  }

  const modalId = `warning-model-${warning.globalId}`.replaceAll(':', '-');
  return <>
    <tr>
      <td>{warning.type}</td>
      <td><span className="font-weight-bold">{getTypeForTarget(target)?.description}</span> {target?.name}</td>
      <td>{dateTimeFormat.format(new Date(warning.lastNotification))}</td>
      <td>{warning.latestEvent.type}</td>
      <td>
        <button type="button" className="btn btn-sm btn-primary" data-toggle="modal" data-target={`#${modalId}`}>
          <i className="icon icon-common icon-search-plus"></i>
        </button>
      </td>
    </tr>
    <div className="modal" tabIndex="-1" id={modalId}>
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {warning.type} on {getTypeForTarget(target)?.description} {target?.name}
            </h5>
            <button type="button" className="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div className="modal-body">
            <h3>Details</h3>
            <CurationWarningDetails warning={warning} target={target} />

            <h3 className="mt-4">Events</h3>
            <CurationWarningEventList eventsUrl={warning?._links?.events.href} />
          </div>
          <div className="modal-footer">
            { warning?.open &&
                <button type="button" className="btn btn-primary m-0 ml-1">
                  <i className="icon icon-common icon-clock mr-1"></i>
                  Snooze
                </button>
            }
            { warning.latestEvent.type === "SNOOZED" &&
                <button type="button" className="btn btn-primary m-0 ml-1">
                  <i className="icon icon-common icon-calendar-check mr-1"></i>
                  Reopen
                </button>
            }
            <button type="button" className="btn btn-danger m-0 ml-1">
              <i className="icon icon-common icon-bomb mr-1"></i>
              Delete
            </button>
          </div>
        </div>
      </div>
    </div>
  </>;
}