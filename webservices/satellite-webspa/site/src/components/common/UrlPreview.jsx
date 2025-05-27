import React, {useCallback, useEffect, useId, useRef, useState} from "react";
import {Collapse} from 'bootstrap';

const EVENT_NAME = "loadIdorgPreview";

export default ({url}) => {
  const [expanded, setExpanded] = useState(false);
  const [shouldLoadPreview, setShouldLoadPreview] = useState(false)

  const elementId = useId();
  const loadPreviewEvent = new CustomEvent(EVENT_NAME, { detail: {elementId} });

  const collapseRef = useRef(null);

  useEffect(() => {
    if (!collapseRef.current) {return;}
    const bsCollapse = new Collapse(collapseRef.current, {toggle: false});
    expanded ? bsCollapse.show() : bsCollapse.hide();
  }, [expanded]);


  const handleLoadPreviewEvent = useCallback((e) => {
    const eventElementId = e.detail.elementId;
    if (elementId !== eventElementId) {
      setExpanded(false);
    }
  }, []);

  const onTogglePreviewClicked = useCallback(() => {
    setShouldLoadPreview(true);
    setExpanded(!expanded)
    if (!expanded) {
      window.dispatchEvent(loadPreviewEvent)
    }
  }, [expanded]);

  useEffect(() => {
    window.addEventListener(EVENT_NAME, handleLoadPreviewEvent);
    return () => {
      window.removeEventListener(EVENT_NAME, handleLoadPreviewEvent);
    }
  }, []);

  return (<>
    <button className="text-muted ms-1 text-decoration-none" type="button" title="Toggle preview"
            onClick={onTogglePreviewClicked}>
      {expanded ?
          <i className="icon icon-common icon-eye-slash"></i> :
          <i className="icon icon-common icon-eye"></i> }
    </button>
    { shouldLoadPreview &&
        <div ref={collapseRef} className="preview-iframe-container bg-white px-2 py-1 mt-1">
          <div className="alert alert-info px-2 py-1">
            If the preview fails to load, it's likely being blocked by the provider.
            When this happens, please check by clicking the identifiers.org URL.
          </div>
          <iframe scrolling="no" sandbox="allow-scripts allow-same-origin"
                  src={url} className="preview-iframe overflow-hidden w-100" />
        </div>
    }
  </  >)

}