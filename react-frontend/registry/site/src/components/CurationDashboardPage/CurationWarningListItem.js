import React, { useState } from "react";
import { config } from "../../config/Config";
import { Link } from "react-router-dom";
import Spinner from "../common/Spinner";

export default ({resourceId, availability}) => {
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);
  const [expanded, setExpanded] = useState(false);

  const [resource, setResource] = useState(null);
  const [namespace, setNamespace] = useState(null);

  if (loading === null) {
    setLoading(true);
    const resourcePromise = fetch(config.registryApi + "/restApi/resources/" + resourceId)
      .then(response => response.json()).then(json => setResource(json));
    const namespacePromise = fetch(config.registryApi + "/restApi/resources/" + resourceId + "/namespace")
      .then(response => response.json()).then(json => setNamespace(json));

    Promise.all([resourcePromise, namespacePromise])
      .catch(() => setFailed(true))
      .finally(() => setLoading(false));
  }

  if (loading) {
    return <div className="card mb-1">
      <div className="card-header py-1 pr-1"> <Spinner compact noText noCenter /> </div>
    </div>;
  }
  if (failed) {
    return <div className="card mb-1">
      <div className="card-header py-1 pr-1"> Failed get information for resource {resourceId}! </div>
    </div>;
  }


  const namespaceUrl = "/registry/" + namespace?.prefix
  const sampleUrl = resource?.urlPattern.replace("{$id}", resource?.sampleId);
  return (<div className="card mb-1">
    <div className="card-header py-1 pr-1 d-flex justify-content-between clear-link"
         onClick={() => setExpanded(!expanded)} title="Click to expand">
      <span>
        {expanded ? <i className="icon icon-common icon-minus mr-2"/> :
          <i className="icon icon-common icon-plus mr-2"/>}
        Resource <strong>{resource?.name}</strong> of
        namespace <strong>{namespace?.name}</strong> has
        availability of <strong>{availability}%</strong>
      </span>
      <Link to={namespaceUrl} className="clear-link btn btn-primary btn-sm px-1 py-0 text-white">
        Go to namespace page
        <i className="icon icon-common icon-arrow-right ml-2"></i>
      </Link>
    </div>
    {expanded &&
      <div className="card-body py-2">
        <h6 className="mt-1 mb-2  ">Check the URLs below and verify if they need to be updated or if the resource should be deprecated.</h6>
        <div className="list-as-two-column-table striped">
          <div className="t-row">
            <div className="text-dark font-weight-bold">Resource Home page:</div>
            <div><a href={resource?.resourceHomeUrl}>{resource?.resourceHomeUrl}</a></div>
          </div>
          <div className="t-row">
            <div className="font-weight-bold">Sample URL:</div>
            <div><a href={sampleUrl} target="_blank">{sampleUrl}</a></div>
          </div>
        </div>
      </div>}
  </div>)
}