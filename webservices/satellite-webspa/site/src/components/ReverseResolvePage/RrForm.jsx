import React from "react";

export default ({initialUrl, onSubmit, accInputRef, urlInputRef}) => (
    <form onSubmit={onSubmit}>
        <div className="row g-2">
          <div className="col">
            <div className="input-group">
                <label htmlFor="url-input" className="input-group-text">Provider URL</label>
                <input ref={urlInputRef} defaultValue={initialUrl} className="form-control form-control-lg"
                       name="url" type="url" value={undefined} required={true}/>
            </div>
            <p className="form-text text-muted">
              Input the URL that you want to generate a identifiers.org URL for.
              Make sure it starts with "http://" or "https://".
            </p>

            <div className="input-group">
                <label htmlFor="accession-input" className="input-group-text">Accession or ID</label>
                <input ref={accInputRef} id="accession-input" name="accession"
                       type="text" className="form-control" value={undefined}/>
            </div>
            <p className="form-text text-muted">
              Optional, but it might help resolution.
              Input the accession or ID of the object in the URL above.
              Normally this should be contained in the URL.
            </p>
          </div>

          <div className="col-12 col-lg-3 col-xl-3 col-xxl-2 d-flex flex-column align-items-stretch pb-4">
              <button className="btn btn-primary text-light py-3" type="submit">
                  <i className="icon icon-common icon-search pe-2" />
                  Convert
              </button>
              <button className="btn btn-secondary text-light mt-2 flex-grow-2" type="reset">
                Clear
              </button>
          </div>
        </div>
    </form>
)