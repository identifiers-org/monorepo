import React from "react";
import PropTypes from "prop-types";

const MetadataCard = ({metadata, parsedCid}) => {
  const provider = metadata?.retrieverId ? metadata?.retrieverId[0] : "unknown";
  const contentElemId = `metadata-content-${provider}`
  let moreInfoHref = getMoreInfoHrefFor(provider, parsedCid);

  const metadataValues = Object.entries(metadata)
      .filter(([k,]) => k !== "retrieverId")
      .flatMap(([k, v]) => v.map((vi, idx) =>
          <li key={`list-group-item-${provider}-${k}-${idx}`} className="list-group-item">{k}:
            <MetadataValue value={vi}/>
          </li>
      ));

  return (
    <div className="metadata-card card mt-3 mb-3">
      <div className="card-header d-flex align-items-center">
        <button title="click to expand or collapse" type="button" data-toggle="collapse"
                data-target={"#" + contentElemId} aria-expanded="false" aria-controls={contentElemId}>
          <span className="collapse-symbol" />
          {GetTitleForRetrieverId(provider)}
        </button>
      </div>
      <div id={contentElemId} className="card-body collapse show p-0">
        <ul className="list-group list-group-flush">
          { metadataValues }
          { moreInfoHref &&
            <li key={`list-group-item-${provider}-more`} className="list-group-item">
              <a href={moreInfoHref} target="_blank" className="clear-link">
                Click here for more
              </a>
            </li>
          }
        </ul>
      </div>


    </div>
  )
}

const MetadataValue = props => {
  if (typeof props.value === "string" && isValidUrl(props.value)) {
    return <a href={props.value}> {props.value} </a>;
  } else {
    return props.value;
  }
}

const getMoreInfoHrefFor = (provider, parsedCid) => {
  switch (provider) {
    case "togoid":
      return `https://togoid.dbcls.jp/?route=&ids=${parsedCid.localId}`;
    default:
      return undefined;
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