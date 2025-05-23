import React, {useState} from "react";
import CurationWarningEventList from "./CurationWarningEventList";

const CurationWarningDetails = ({curationWarning}) => {
  const [expanded, setExpanded] = useState(false)

  const showMoreInfo = !["curator-review"].includes(curationWarning.type)

  return <div className="card mb-2">
    <div className="card-header p-1 px-3">
      <button className="btn btn-link text-decoration-none text-reset p-0 me-2"
              onClick={() => setExpanded(!expanded)}>
        { expanded ?
            <i className="icon icon-common icon-caret-up me-1" /> :
            <i className="icon icon-common icon-caret-down me-1" />
        }
        { getLabelFor(curationWarning) }
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