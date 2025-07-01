import React, {useEffect, useState} from "react";
import {config} from "../../../config/Config";
import Spinner from "../../common/Spinner";
import CurationWarningDetails from "./CurationWarningDetails";
import {Link} from "react-router-dom";
import {renewToken} from "../../../utils/auth";

const allSettledAndSuccessfulJsons = (fetches) =>
    Promise.allSettled(fetches)
        .then(results => results.filter(r =>
            r.status === "fulfilled" && r.value.status === 200
        ))
        .then(results => Promise.all(results.map(r => r.value.json())))


export default ({selectedTarget, modalId, editHref}) => {
  const [loading, setLoading] = useState(null);
  const [failed, setFailed] = useState(false);
  const [curationWarnings, setCurationWarnings] = useState([])


  useEffect(() => {
    const fn = async () => {
      if (loading !== true && selectedTarget) {
        setLoading(true);
        setCurationWarnings([]);

        const authToken = await renewToken();
        const init = { headers: { 'Authorization': `Bearer ${authToken}` } }

        const targetUrl = selectedTarget.type === 'Institution' ?
            config.registryApi + `/restApi/institutions/` + selectedTarget.identifier :
            config.registryApi + `/restApi/namespaces/search/findByPrefix?prefix=` + selectedTarget.identifier;
        fetch(targetUrl, init)
            .then(response => response.json())
            .then(target => {
              const directCws = fetch(target._links.curationWarnings.href, init);
              if (selectedTarget.type === 'Prefix') {
                // Fetch CW under namespace and under its resources
                fetch(target._links.resources.href, init)
                    .then(resp => resp.json())
                    .then(resources => {
                      const cwPromises = [
                        directCws,
                        ...resources._embedded.resources.map(res => fetch(res._links.curationWarnings.href, init))
                      ]
                      allSettledAndSuccessfulJsons(cwPromises)
                          .then(jsons => jsons.flatMap(json => json._embedded.curationWarnings))
                          .then(warnings => warnings.filter(cw => cw.open))
                          .then(warnings => setCurationWarnings(warnings))
                          .catch(() => setFailed(true))
                          .finally(() => setLoading(false))
                    })
                    .catch(() => setFailed(true))
              } else {
                directCws.then(response => response.json())
                    .then(json => {
                      setCurationWarnings(json._embedded.curationWarnings)
                      setFailed(false)
                    })
                    .catch(() => setFailed(true))
                    .finally(() => setLoading(false))
              }
            })
            .catch(() => setFailed(true))
      }
    }
    fn();
  }, [selectedTarget, setFailed, setLoading, setCurationWarnings]);


  let body;
  if (!selectedTarget)
    body = <></>;
  else if (loading)
    body = <Spinner noText/>
  else if (failed)
    body = <div className="alert alert-danger"> Failed to get warnings! </div>
  else
    body = curationWarnings.map((cw, idx) =>
      <CurationWarningDetails key={idx} curationWarningInit={cw} targetInfo={selectedTarget}/>
    )

  return (
      <div className="modal" id={modalId} tabIndex={-1}>
        <div className="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
          <div className="modal-content">
            <div className="modal-header">
              <h6 className="modal-title">
                Open warnings for {selectedTarget?.label} ({selectedTarget?.identifier})
                <Link to={editHref} target="_blank" className="ms-2">
                  <i className="icon icon-common icon-external-link-alt"></i>
                </Link>
              </h6>
              <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"/>
            </div>
            <div className="modal-body">
              {body}
            </div>
          </div>
        </div>
      </div>
  )
}