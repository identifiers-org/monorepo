import React from "react";
import PropTypes from "prop-types";

const MetadataCard = ({metadata}) => {
  const provider = metadata?.retrieverId ? metadata?.retrieverId[0] : "unknown";
  const contentElemId = `metadata-content-${provider}`
  let uiCounter = 0;
  return (
    <div className="card mt-3 mb-3">
      <div className="card-header d-flex align-items-center">
        <button title="click to expand or collapse" type="button" data-toggle="collapse"
                data-target={"#" + contentElemId} aria-expanded="false" aria-controls={contentElemId}>
          {GetTitleForRetrieverId(provider)}
        </button>
      </div>
      <ul id={contentElemId} className="list-group list-group-flush collapse show">
        {Object.entries(metadata)
          .filter(([k,]) => k !== "retrieverId")
          .flatMap(([k, v]) =>
            v.map(vi => <li key={uiCounter++} className="list-group-item">{k}: <MetadataValue value={vi}/></li>)
          )
        }
      </ul>
    </div>
  )
}

const MetadataValue = props => {
  if (typeof props.value === "string" && isValidUrl(props.value)) {
    return <a href={props.value}>{props.value}</a>;
  } else {
    return props.value;
  }
}

const isValidUrl = urlString => {
  try {
    return Boolean(new URL(urlString));
  } catch (e) {
    return false;
  }
}

const GetTitleForRetrieverId = (id) => {
  switch (id) {
    case "ebisearch":
      return <>Subset of indexed fields from <a target="_blank" href="https://www.ebi.ac.uk/ebisearch">EBI Search</a></>;
    case "togoid":
      return <>Subset of related URIs in <a target="_blank" href="https://togoid.dbcls.jp/">TogoID</a> database</>;
    default:
      return id;
  }
}
  
  
MetadataCard.propTypes = {
  metadata: PropTypes.object
}
export default MetadataCard;