import React, {useState} from "react";
import ReverseSearchMatch from "./ReverseSearchMatch";
import {config} from "../../config/Config";

const isUrl = string => {
  if (!string || !string.startsWith("http")) { return false; }
  try { return Boolean(new URL(string)); }
  catch(e) { return false; }
}

const exampleUrls = {
  IntAct: "https://www.ebi.ac.uk/intact/interaction/EBI-2307691",
  ENA: "https://www.ebi.ac.uk/ena/browser/view/BN000065",
  UniProt: "http://purl.uniprot.org/uniprot/P0DP23"
}

export default () => {
  const [url, setUrl] = useState('')
  const [match, setMatch] = useState(null)
  const [loading, setLoading] = useState(null);

  const handleExampleClick = (e) => {
    setUrl(e.target.dataset.idorgExample);
    setTimeout(() => document.getElementById("reverseResolveButton")?.click(), 50);
  }

  const handleInputChange = (e) => {
    const {value} = e.target;
    setMatch(null);
    setLoading(null)
    setUrl(value);
  }

  const handleSubmit = async (e) => {
    e.preventDefault(); e.stopPropagation();

    setLoading(true);
    fetch (config.resolverApi + "/reverse/byPrefix", {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({apiVersion: "1.0", payload: {url}})
    })
      .then(res => res.status === 200 ? res.json() : null, console.error)
      .then(match => setMatch(match ? match.payload : {}))
      .finally(() => setLoading(false));
  }

  return (
      <div>
        <small className="form-text text-muted ms-1">
          Examples:{' '}
          {
            Object.entries(exampleUrls).map(([provider, url]) => (
                <button role='suggestion' key={provider} tabIndex='0'
                        type="button" className='text-primary me-2'
                        onClick={handleExampleClick}
                        onKeyDown={handleExampleClick}
                        title={url}
                        data-idorg-example={url}>
                  {provider}
                </button>
            ))
          }
        </small>
        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <input
                role="searchbox"
                spellCheck={false}
                className="form-control search-input"
                placeholder="Enter an URL which you wish to link to via identifiers.org"
                onChange={handleInputChange}
                value={url}
            />
            <button id="reverseResolveButton"
                className="btn btn-primary text-white search-button"
                disabled={isUrl(url) && !loading ? undefined : true}
                type="submit"
            >
              <i className="icon icon-common icon-search pe-2" />
              {loading ? "Converting..." : "Convert" }
            </button>
            <ReverseSearchMatch url={url} match={match} loading={loading} />
          </div>
          <a className="text-muted text-sm ms-1 text-decoration-none"
             href="https://docs.identifiers.org/pages/api#resolution-api-services" target="_blank">
            powered by our resolution API
          </a>
        </form>
      </div>
    )
}

