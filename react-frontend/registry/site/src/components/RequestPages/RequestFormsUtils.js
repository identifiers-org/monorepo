import React, {useEffect, useState} from "react";
import {connect} from "react-redux";

import getInstitutionForRORIDFromRegistry from "../../utils/getInstitutionRorIdFromRegistry";
import {swalError} from "../../utils/swalDialogs";
import {getNamespacesFromRegistry} from "../../actions/NamespaceList";
import {useWatch} from "react-hook-form";
import RegistrationRequestField from "./RegistrationRequestField";


export const setAllFlags = {
  shouldValidate: true,
  shouldDirty: true,
  shouldTouch: true
}


export const handleInstitutionIsProviderChange = (setInstitutionIsProvider, formContext) =>
  (event) => {
    const {
      setValue,
      getValues,
      formState: {defaultValues}
    } = formContext;
    const source = event.target.checked ? getValues() : defaultValues;
    setValue("providerName", source.institutionName, setAllFlags);
    setValue("providerDescription", source.institutionDescription, setAllFlags);
    setValue("providerHomeUrl", source.institutionHomeUrl, setAllFlags);
    setValue("providerLocation", source.institutionLocation, setAllFlags);
    setInstitutionIsProvider(event.target.checked);
  }

const rorDebounce = React.createRef(-1);
export const handleRorAutocomplete = (formContext) => {
  const func = (event) => {
    const {
      getFieldState,
      setValue,
      getValues,
      formState,
    } = formContext;

    const {invalid} = getFieldState("institutionRorId", formState);
    const hasDescription = Boolean(getValues("institutionDescription"));
    if (!invalid && event.target.value) {
      getInstitutionForRORIDFromRegistry(event.target.value)
          .then(rorIdInformation => {
            if (rorIdInformation !== null) {
              setValue('institutionName', rorIdInformation.name, setAllFlags)
              setValue('institutionHomeUrl', rorIdInformation.homeUrl, setAllFlags)
              !hasDescription && setValue('institutionDescription', rorIdInformation.description, setAllFlags);
              setValue('institutionLocation', rorIdInformation.location.countryCode, setAllFlags)
            }
          }).catch(e => swalError.fire({
        icon: "error",
        title: "Error on ROR Retrieval",
        text: e.message
      }))
    }
  }
  return (event) => {
    clearTimeout(rorDebounce.current);
    rorDebounce.current = setTimeout(() => func(event), 100);
  }
}






const DisconnectedPrefixAutoCompleter = (props) => {
  const [ selected, setSelected ] = useState(false);
  const [ focused, setFocused ] = useState(false)
  const [ debouncer, setDebouncer ] = useState(0)

  const onWatchedChanged = () => {
    clearTimeout(debouncer)
    setDebouncer(
      setTimeout(
      () => {
        const watched = document.getElementById(props.watchedId);
        setSelected(false)
        if (watched.value)
          props.getNamespacesFromRegistry({content: watched.value});
      }, 75)
    )
  }

  const onClick = (event) => {
    setSelected(true);
    props.setValue(event.target.innerText)
  }

  const onSuggestionMouseOver = (e) =>
    e.target.classList.add("suggestion__selected", "text-white")
  const onSuggestionMouseOut = (e) =>
    e.target.classList.remove("suggestion__selected", "text-white")


  const onFocus = () =>
    setFocused(true);
  const onLoseFocus = () => //Delay to make sure element stays on screen long enough for click
    setTimeout(() => setFocused(false), 100);

  useEffect(() => {
    const watched = document.getElementById(props.watchedId);
    // if (watched.value) new Promise(() => onWatchedChanged());
    watched.addEventListener('input', onWatchedChanged);
    watched.addEventListener('focus', onFocus);
    watched.addEventListener('blur', onLoseFocus);
    return () => {
      watched.removeEventListener('input', onWatchedChanged);
      watched.removeEventListener('focus', onFocus);
      watched.removeEventListener('blur', onLoseFocus);
    }
  });

  if (selected || !focused)
    return undefined
  else {
    return (
      <div className="inline-search-container">
        <div className="suggestions-box">
          <div className="row mx-1">
            <div className="col align-self-end"><p className="text-muted text-right my-0"><small>Suggestions</small></p>
            </div>
          </div>
          { props.namespaceList.length === 0 ? <span className="suggestion">No namespaces found</span> :
            <ul className="suggestion-list pb-2">
              { props.namespaceList.map(suggestion =>
                <li key={`sugg-${suggestion.prefix}`} className="px-2 py-1" onClick={onClick}
                    onMouseOver={onSuggestionMouseOver}
                    onMouseOut={onSuggestionMouseOut}>
                  <span>{suggestion.prefix}</span>
                </li>
              )}
            </ul>
          }
        </div>
      </div>
    )
  }
}

const mapStateToProps = (state) => ({
  namespaceList: state.registryBrowser.namespaceList
});
const mapDispatchToProps = (dispatch) => ({
  getNamespacesFromRegistry: (params) => dispatch(getNamespacesFromRegistry(params))
});
export const PrefixAutoCompleter = connect(mapStateToProps, mapDispatchToProps)(DisconnectedPrefixAutoCompleter)

export const ProtectedUrlFormFields = (props) => {
  const {control, resetField, trigger} = props;
  const renderProtectedUrlInputs = useWatch({
    name: "protectedUrls",
    control: control,
  })
  useEffect(() => {
    if (renderProtectedUrlInputs) {
      trigger(["authHelpDescription", "authHelpUrl"])
    } else {
      resetField("authHelpDescription");
      resetField("authHelpUrl");
    }
  }, [renderProtectedUrlInputs]);

  return <>
    <RegistrationRequestField
        id="protectedUrls"
        description="Do links require users to authenticate to access information?"
        label="Are links protected?"
        type="checkbox"
        control={control}
    />

    {renderProtectedUrlInputs && <>
      <RegistrationRequestField
          id="authHelpDescription"
          description="A short text describing the need for authentication and how to authenticate.
                                   This should be a little paragraph to give some information to users.
                                   The URL bellow should be where users find further details."
          label="Authentication description"
          type="textarea"
          disabled={!renderProtectedUrlInputs}
          control={control}
      />

      <RegistrationRequestField
          id="authHelpUrl"
          description="URL for users to get details on how to authenticate to access resource"
          label="Authentication details URL"
          type="text"
          disabled={!renderProtectedUrlInputs}
          control={control}
      />
    </>}
  </>
}