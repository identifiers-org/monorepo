import {useLocation} from "react-router-dom";
import {useCallback, useEffect, useRef, useState} from "react";
import RrForm from "../ReverseResolvePage/RrForm";
import RrResults from "../ReverseResolvePage/RrResults";
import {swalToast} from "../../utils/swalDialogs";
import {config} from "../../config/Config";





export default () => {
  const { state: locationState } = useLocation();
  const initialUrl = locationState ? locationState.url : '';

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [simResults, setSimResults] = useState([]);
  const [prefixResult, setPrefixResult] = useState(null);
  const urlInputRef = useRef();
  const accInputRef = useRef();

  useEffect(() => {
    if (initialUrl) {
      document.querySelector("button[type='submit']").click();
    }
  }, []);


  const handleError = useCallback((reason) => {
    swalToast.fire({
      icon: 'error',
      title: reason
    })
    console.error(reason)
    setError(true);
    setSimResults([]);
    setPrefixResult({});
  }, [setError, setSimResults, setPrefixResult])
  const handleSuccess = useCallback(([prefResults, simResults]) => {
    setError(false);
    setSimResults(simResults.payload);
    setPrefixResult(prefResults.payload);
  }, [setError, setSimResults, setPrefixResult]);




  const onSubmit = useCallback(async (e) => {
    e.preventDefault(); e.stopPropagation();

    if (urlInputRef.current && !urlInputRef.current.checkValidity()) {
      await swalToast.fire({
        icon: 'error',
        title: urlInputRef.current.validationMessage
      })
      return;
    }

    const url = urlInputRef.current?.value;
    const accessionStr = accInputRef.current?.value;
    const accession = !accessionStr || accessionStr.trim().length === 0 ? undefined : accessionStr;

    setLoading(true);

    const fetchOpts = {
      method: 'POST',
      body: JSON.stringify({apiVersion: "1.0", payload: {url, accession}}),
      headers: {'Content-Type': 'application/json'}
    };
    const prefixFetch = fetch(config.resolverApi + "/reverse/byPrefix", fetchOpts)
    const simFetch = fetch(config.resolverApi + "/reverse/bySimilarity", fetchOpts)
    Promise.all([prefixFetch, simFetch])
        .then(([r1, r2]) => {
          const p1 = r1.status === 200 ?
            r1.json() : Promise.resolve({payload: null})
          const p2 = r2.status === 200 ?
              r2.json() : Promise.resolve({payload: []})
          return Promise.all([p1, p2])
        })
        .then(handleSuccess)
        .catch(handleError)
        .finally(() => setLoading(false));
  }, [handleError, handleSuccess, setLoading, urlInputRef, accInputRef]);




  return (
      <div>
        <div>
          <RrForm onSubmit={onSubmit} initialUrl={initialUrl}
                  urlInputRef={urlInputRef} accInputRef={accInputRef} />
        </div>
        <div className="mt-5 mb-5">
          <RrResults loading={loading} error={error}
                     simResults={simResults} prefixResult={prefixResult} />
        </div>
      </div>
  )
}