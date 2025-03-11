import React, {useCallback, useEffect, useState} from "react";
import PropTypes from "prop-types";
import {config} from "../../config/Config";
import MetadataCard from "./MetadataCard";

const MetadataView = ({curie}) => {
  const [loading, setLoading] = useState(false);
  const [successful, setSuccessful] = useState(false);
  const [metadataValues, setMetadataValues] = useState(null);
  const [parsedCid, setParsedCid] = useState(null);

  const handleMetadataPayload = useCallback(payload => {
    if (payload === null || payload === undefined) {
      setSuccessful(false);
      setLoading(false)
    } else {
      const fetchHeaders = {headers: {accept: "application/json"}};
      setParsedCid(payload.parsedCompactIdentifier)
      const retrieverFetches = payload.ableRetrievers.map(url => fetch(url, fetchHeaders))
      Promise.allSettled(retrieverFetches)
        .then(results => results.filter(r => r.status === "fulfilled"))
        .then(fulfilledResults =>
          Promise.all(fulfilledResults.filter(r => r.value.status === 200).map(r => r.value.json()))
        )
        .then(metadataValues => {
          setMetadataValues(metadataValues)
          setSuccessful(true)
          setLoading(false)
        })
    }
  }, [setSuccessful, setLoading, setMetadataValues, setParsedCid])
  
  useEffect(() => {
    if (!loading && metadataValues === null) {
      setLoading(true);
      fetch(config.metadataRetrieverApiBaseUrl + curie)
        .then(r => r.status === 200 ? r.json() : null)
        .then(r => r.payload)
        .then(handleMetadataPayload)
    }
  }, [loading, metadataValues, setLoading, handleMetadataPayload, config, curie]);
  
  const wrapperClassName = "col-12 pt-3 pt-lg-0 px-0 pl-md-2 ";
  if (loading) {
    return (
      <div className={wrapperClassName + "col-lg-2"}>
        <small className="text-muted">
          Checking for metadata providers <svg width="14" height="14" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path
              d="M10.14,1.16a11,11,0,0,0-9,8.92A1.59,1.59,0,0,0,2.46,12,1.52,1.52,0,0,0,4.11,10.7a8,8,0,0,1,6.66-6.61A1.42,1.42,0,0,0,12,2.69h0A1.57,1.57,0,0,0,10.14,1.16Z"
              className="spinner_P7sC"/>
          </svg>
        </small>
      </div>
    );
  } else if (!successful) {
    return (
      <div className={wrapperClassName + "col-lg-2"}>
        <small className="text-muted">
          Failed to retriever metadata! Please contact our team.
        </small>
      </div>
    );
  } else if (metadataValues?.length === 0) {
    return (
      <div className={wrapperClassName + "col-lg-2"}>
        <small className="text-muted">
          Zero metadata providers found
        </small>
      </div>
    );
  } else {
    return <div className={wrapperClassName + " col-lg-5"}>
      <small className="text-muted">
        Found metadata in {metadataValues?.length} {metadataValues?.length === 1 ? 'provider' : 'providers'}.
        These are acquired via
        our <a target="_blank" href="https://docs.identifiers.org/pages/metadata_service.html">metadata service API</a>.
      </small>
      {metadataValues?.map((m, i) => <MetadataCard key={"view_" + i} metadata={m} parsedCid={parsedCid}/>)}
    </div>
  }
}

MetadataView.propTypes = {
  curie: PropTypes.string.isRequired
}
export default MetadataView;