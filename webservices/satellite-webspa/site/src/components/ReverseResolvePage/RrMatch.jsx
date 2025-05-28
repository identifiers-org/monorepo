import React from "react";
import {copyToClipboard} from "../../utils/copyToClipboard";
import UrlPreview from "../common/UrlPreview";

export default ({match}) => {
    if (!match) {
        return undefined;
    }

    const luiPatternBadge = {
        class: match.lui_pattern_match ? 'bg-success' : 'bg-warning text-dark',
        text: match.lui_pattern_match ? "Matches LUI pattern" : "Doesn't match LUI pattern"
    };

    const roundedScore = Math.floor(match.similarity_score);
    const similarityScoreBadgeClass = roundedScore >= 90 ?
        'bg-success' :
        (roundedScore < 50 ? 'bg-danger' :
            'bg-warning text-dark');

    return (
        <div className="card h-100 similarity-match">
            <div className="card-header">
                Prefix <b>{match.prefix}</b>
                <a className="ms-1 text-primary" target="_blank"
                   href={"https://registry.identifiers.org/registry/"+match.prefix}>
                    <i className="icon icon-common icon-external-link-alt ms-1 me-2"></i>
                </a>
            </div>
            <div className="card-body">
                <div>
                    <span className={ "badge " + luiPatternBadge.class }>
                        {luiPatternBadge.text}
                    </span>
                </div>

                <div>
                    <span className={ "badge " + similarityScoreBadgeClass }>
                        Similarity score: {roundedScore}%
                    </span>
                </div>
            </div>
            <div className="card-footer w-100">
                { match.possible_idorg_url ? <>
                        <a target="_blank" className="text-reset" href={match.possible_idorg_url}>
                            <span className="me-1">{match.possible_idorg_url}</span>
                        </a>
                        <button type="button" title="copy" onClick={(e) =>
                            copyToClipboard(match.possible_idorg_url, e)}>
                            <i className="icon icon-common icon-copy text-body-secondary"></i>
                        </button>
                        <UrlPreview url={match.possible_idorg_url} />
                    </>: <>
                        <span className="me-1">Could not generate valid URL</span>
                        <button type="button" title="Doesn't match local unique identifier pattern">
                            <i className="icon icon-common icon-question-circle text-body-secondary"></i>
                        </button>
                    </>
                }
            </div>
        </div>
    )
}
