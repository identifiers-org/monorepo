import React, {useCallback, useEffect, useState} from "react";
import CurationWarningEventList from "./CurationWarningEventList";
import {config} from "../../../config/Config";
import {swalError} from "../../../utils/swalDialogs";


const CurationWarningDetails = ({curationWarningInit}) => {
  const [curationWarning, setCurationWarning] = useState(curationWarningInit)
  const [expanded, setExpanded] = useState(false)

  const isDisabled = curationWarning?.latestEvent?.type === "DISABLED";




  const renewCurationWarning = useCallback(async () => {
    await fetch (curationWarning._links.self.href)
        .then(
            async response => {
              if (response.ok) {
                const renewedCurationWarning = await response.json()
                setCurationWarning(renewedCurationWarning)
                setExpanded(false); // Lazy way to force refresh
              } else {
                await swalError.fire({
                  title: 'Error',
                  text: 'Could not renew information: ' + reason
                });
              }
            },
            async reason => {
              await swalError.fire({
                title: 'Error',
                text: 'Could not renew information: ' + reason
              });
            }
        )
  }, [isDisabled, curationWarning]);

  const toggleDisabledStatus = useCallback(async () => {
    const cwId = curationWarning._links.self.href.split('/').pop();
    const endpoint = isDisabled ?
        "/curationApi/enable?" :
        "/curationApi/disable?";
    const params = new URLSearchParams({
      id: cwId
    })

    await fetch (config.registryApi + endpoint + params)
        .then(
            async response => {
              if (response.ok) {
                await renewCurationWarning()
              } else {
                await swalError.fire({
                  title: 'Error',
                  text: 'Could not change status: HTTP response ' + response.status
                });
              }
            },
            async reason => {
              await swalError.fire({
                title: 'Error',
                text: 'Could not change status: ' + reason
              });
            }
        )
  }, [isDisabled, curationWarning, renewCurationWarning]);





  const showMoreInfo = !["curator-review"].includes(curationWarning.type)
  const bgClass = isDisabled ? " bg-danger-subtle" : "";
  const txtClass = isDisabled ? " text-decoration-line-through" : "";

  return <div className="card mb-2">
    <div className={`card-header d-flex p-1 px-3${bgClass}`}>
      <button className={`btn btn-link text-decoration-none text-reset text-start p-0 me-2 flex-grow-1${txtClass}`}
              onClick={() => setExpanded(!expanded)}>
        { expanded ?
            <i className="icon icon-common icon-caret-up me-1" /> :
            <i className="icon icon-common icon-caret-down me-1" />
        }
        { getLabelFor(curationWarning) }
      </button>
      <button className={`btn btn-sm rounded-pill py-0 btn-${isDisabled ? "success" : "danger"}`}
              onClick={toggleDisabledStatus}>
        {isDisabled ? "Enable" : "Disable"}
      </button>
    </div>
    {expanded &&
        <div className="card-body p-1 px-3">
          { showMoreInfo && getDescriptionFor(curationWarning) }
          <CurationWarningEventList href={curationWarning._links.events.href} preload={!showMoreInfo} />
        </div>
    }
  </div>
}
export default CurationWarningDetails;



const kebabCaseToSpaceSeparated = (str) => {
  const fieldName = str.replaceAll("-", " ");
  return String(fieldName).charAt(0).toUpperCase() + String(fieldName).slice(1);
}

const getLabelFor = cw => {
  switch (cw.type) {
    case "curator-review":
      const fieldName = kebabCaseToSpaceSeparated(cw['moreDetails']['field-name'])
      return `${fieldName} value is empty or set to CURATOR_REVIEW`
    case "wikidata-institution-diff":
      return "Possible discrepancy with Wikidata"
    case "url-not-ok":
      return "Invalid response from URL"
    case "low-availability-resource":
      return "Resource URL measuring low availability"
    default:
      return cw.type
  }
}


const getDescriptionFor = cw =>
    <table className="table table-striped mb-0">
      <tbody>
      { Object.entries(cw.moreDetails).map( ([key, value], idx) =>
          <tr key={idx}>
            <td>{kebabCaseToSpaceSeparated(key)}</td>
            <td><AnchorOrText str={value}/></td>
          </tr>
      )}
      </tbody>
    </table>

const AnchorOrText = props => {
  if (props.str.startsWith("http")) {
    return <a href={props.str}>{props.str}</a>
  } else {
    return props.str;
  }
}