import {Link} from "react-router-dom";
import {copyToClipboard} from "../../utils/copyToClipboard";
import React from "react";
import UrlPreview from "../common/UrlPreview";

export default ({url, match, loading}) => {
  let isWarning = false;
  let body;
  if (loading === null || loading === true || !match) {
    return undefined;
  } else if (Object.keys(match).length === 0) {
    body = <>
      <div>We could not find an exact match for this URL</div>
      <Link to="/reverseResolve" state={{url}}
            className="btn btn-link fs-6 text-decoration-none m-0 p-0">
        Click here to search by similarity
      </Link>
    </>
  } else {
    isWarning = !match.lui_pattern_match;

    body = (
        <>
          <div className="mb-2">
            Match with prefix{' '}
            <a href={'https://registry.identifiers.org/registry/' + match.prefix}
               target={"_blank"} className="text-decoration-none">
              <b>{match.prefix}</b>
              <i className="icon icon-common icon-external-link-alt ms-1 me-2"></i>
            </a>
            <hr className="m-0 p-0"/>
          </div>

          {!match.lui_pattern_match && <div>
            However, the LUI pattern does not match, and might need to be updated.
            Please contact us using the feedback button above.
          </div>}

          {match.possible_idorg_url && <div>
            Likely URL:
            <a href={match.possible_idorg_url} target="_blank">
              {match.possible_idorg_url}
            </a>
            <button className='text-muted ms-1 text-decoration-none'
                    title='copy to clipboard' type="button"
                    onClick={(ev) =>
                        copyToClipboard(match.possible_idorg_url, ev)}
            >
              <i className="icon icon-common icon-copy"></i>
            </button>
            <UrlPreview url={match.possible_idorg_url} />

            <div>
              <i className="icon icon-common icon-exclamation-triangle"></i>
              <span className="mx-1">Make sure it links where you expect before publishing this anywhere</span>
              <i className="icon icon-common icon-exclamation-triangle"></i>
            </div>
          </div>
          }
        </>
    );
  }

  return <div id="reverse-match-container" className="inline-search-container">
    <div className={"hints-box py-2 px-3 " + (isWarning ? "alert alert-warning mb-0" : "bg-white")}>
      {body}
    </div>
  </div>

}