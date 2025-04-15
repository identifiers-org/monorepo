import React, {useCallback, useEffect, useRef, useState} from "react";
import PropTypes from "prop-types";
import { Collapse } from 'bootstrap';

const MetadataCard = ({metadata, parsedCid}) => {
  const provider = metadata?.retrieverId ? metadata?.retrieverId[0] : "unknown";
  const contentElemId = `metadata-content-${provider}`
  let moreInfoHref = getMoreInfoHrefFor(provider, parsedCid);

  const [expanded, setExpanded] = useState(true)
  const collapseRef = useRef(null);
  useEffect(() => {
    if (!collapseRef.current) {return;}
    const bsCollapse = new Collapse(collapseRef.current, {toggle: false});
    expanded ? bsCollapse.show() : bsCollapse.hide();
  });

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
        <button title="click to expand or collapse" type="button" onClick={() => setExpanded(!expanded)}>
          <span> {expanded ? '▲' : '▼'} </span>
          {GetTitleForRetrieverId(provider)}
        </button>
      </div>
      <div ref={collapseRef} className="card-body collapse p-0">
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
    return <a href={props.value} className="text-decoration-none"> {props.value} </a>;
  } else {
    return <>&nbsp;props.value</>;
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
      return <>
        Subset of indexed fields from&nbsp;
        <a onClick={e => e.stopPropagation()}
           target="_blank"
           className="text-decoration-none"
           href="https://www.ebi.ac.uk/ebisearch">
          EBI Search
        </a>
      </>;
    case "togoid":
      return <>
        Subset of related URIs in&nbsp;
        <a onClick={e => e.stopPropagation()}
           target="_blank"
           className="text-decoration-none"
           href="https://togoid.dbcls.jp/">
          TogoID
        </a> database
      </>;
    default:
      return id;
  }
}
  
  
MetadataCard.propTypes = {
  metadata: PropTypes.object
}
export default MetadataCard;