import React from "react";
import RrMatch from "./RrMatch";
import Spinner from "../common/Spinner";

export default ({loading, error, simResults, prefixResult}) => {
    if (simResults.length === 0 && !prefixResult) {
        return null;
    } else if (loading) {
        return <Spinner/>;
    } else if (error) {
        return <div className="alert alert-danger" role="alert">An error has occurred!</div>;
    }

    return (
        <div>
            <div className="alert alert-warning d-flex flex-nowrap justify-content-between align-items-center" role="alert">
              <i className="icon icon-common icon-exclamation-triangle display-1"></i>
              <span className="mx-2 text-center">
                  <b>Beware that identifiers.org URLs below this notice are tentative</b><br/>
                  They might direct to some other object or be completely invalid<br/>
                  Make sure to test them before use
              </span>
              <i className="icon icon-common icon-exclamation-triangle display-1"></i>
            </div>
            { prefixResult && <div>
                <h2 className="mb-1">
                  URL prefix match
                  <button type="button" className="cursor-help ms-1"
                          title="Matches URL against registry patterns exactly">
                    <i className="icon icon-common icon-question-circle fs-4"></i>
                  </button>
                </h2>
              <hr className="mt-0 mb-3"/>
                <div>
                    <RrMatch match={prefixResult} />
                </div>
            </div> }
            <div className="mt-4">
                <h3 className="mb-1">
                  Similarity matches
                  <button type="button" className="cursor-help ms-1"
                          title="Topmost similar registry patterns currently on registry">
                    <i className="icon icon-common icon-question-circle fs-4"></i>
                  </button>
                </h3>
                <hr className="mt-0 mb-3"/>
                <div className="row align-items-stretch g-2">
                    {
                        simResults?.length === 0 &&
                            <span>No similar matches could be retrieved</span>
                    }
                    { simResults?.map(((match, idx) =>
                            <div className="col-4" key={idx}>
                                <RrMatch match={match} />
                            </div>
                    ))}
                </div>
            </div>
        </div>
    )
}
