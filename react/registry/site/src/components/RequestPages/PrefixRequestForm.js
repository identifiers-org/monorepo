import React, {useEffect, useState} from "react";
import {connect} from 'react-redux';

import RegistrationRequestField from "./RegistrationRequestField";
import {LoadFormButton, SaveFormButton} from "./LocalStorageButtons";
import {handleInstitutionIsProviderChange, handleRorAutocomplete, ProtectedUrlFormFields} from "./RequestFormsUtils"

import Spinner from "../common/Spinner";
import PrefixRegistrationRequestSchema, {PrefixRequestInitialValues} from "./PrefixRegistrationRequestSchema";
import {yupResolver} from "@hookform/resolvers/yup";
import {useForm} from "react-hook-form";
import {onPrefixSubmit} from "./RequestSubmitHandlers";

const PrefixRequestForm = (props) => {
  const [institutionIsProvider, setInstitutionIsProvider] = useState(false);
  const methods = useForm({
    resolver: yupResolver(PrefixRegistrationRequestSchema),
    defaultValues: PrefixRequestInitialValues,
    mode: 'onBlur',
    reValidateMode: 'onBlur'
  });
  const {
    handleSubmit,
    formState,
    getValues,
    reset,
    control,
    resetField,
    trigger
  } = methods;
  const {isValid} = formState;

  useEffect(() => {
    const rorIdInput = document.querySelector("#institutionRorId");
    rorIdInput.addEventListener('blur', handleRorAutocomplete(methods));
    return () => {
      rorIdInput.removeEventListener('blur', handleRorAutocomplete(methods));
    }
  });

  return (
    <form data-matomo-form="" data-matomo-name="cloud_login"
          className="form" autoComplete="off" onSubmit={handleSubmit(onPrefixSubmit)}>
      <div className="card mb-3">
        <div className="card-header">
          <h2 className="mb-3"><i className="icon icon-common icon-leaf"/> Namespace Details</h2>
          <p className="text-muted">
            This section collects information related to the new ID space that is being requested,
            including its requested prefix, e.g.&nbsp;
            <span className="text-dark">pdb</span>,&nbsp;
            <span className="text-dark">uniprot</span> or&nbsp;
            <span className="text-dark">kegg.genes</span>.
          </p>
        </div>

        <div className="card-body">
          <RegistrationRequestField
            id="name"
            description="The name of the new ID space."
            example="Protein Data Bank"
            label="Namespace Name"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="description"
            description="Short description of the ID space in one or multiple sentences."
            example="The Protein Data Bank is the single worldwide archive of structural
                        data of biological macromolecules"
            label="Description"
            rows="5"
            type="textarea"
            control={control}
          />

          <RegistrationRequestField
            id="requestedPrefix"
            description="Character string meant to precede the colon in resolved identifiers. No spaces or
                        punctuation, only lowercase alphanumerical characters, underscores and dots."
            example="pdb"
            label="Requested Prefix"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="sampleId"
            description="An example local identifier."
            example="2gc4"
            label="Sample Id"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="idRegexPattern"
            description="A regular expression definition of the IDs in this namespace."
            example="^[0-9][A-Za-z0-9]{3}$"
            label="Regex pattern"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="supportingReferences"
            description="Supporting references (URLs, citations), if any, to published work describing the resource.
                        Enter one per line."
            example="https://doi.org/10.1093/nar/28.1.235"
            label="Supporting references"
            rows="5"
            type="textarea"
            control={control}
          />

          <RegistrationRequestField
            id="additionalInformation"
            description="Anything else you wish to tell or ask us."
            label="Additional information"
            required={false}
            rows="5"
            type="textarea"
            control={control}
          />
        </div>
      </div>

      <div className="card mb-3">
        <div className="card-header">
          <h2 className="mb-2"><i className="icon icon-common icon-sitemap"/> Institution details</h2>
          <p>
            This section of the form collects the information of the institution that runs the first provider being
            registered
            in the new namespace being requested. Examples are EMBL-EBI, Kyoto University
            or NCBI.
          </p>
        </div>

        <div className="card-body">
          <RegistrationRequestField
            id="institutionRorId"
            description="The ROR ID of the organization."
            example={<>
              <a href="https://ror.org/02catss52" target="_blank">https://ror.org/02catss52</a> or <a href="https://ror.org/05cy4wa09" target="_blank">https://ror.org/05cy4wa09</a>
            </>}
            label="ROR ID"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="institutionName"
            description="The name of the institution that runs the provider."
            example="European Bioinformatics Institute, Hinxton, Cambridge, UK"
            label="Name"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="institutionDescription"
            description="Short description of the institution in one or multiple sentences."
            example="The European Bioinformatics Institute (EMBL-EBI) is the part of EMBL
                        dedicated to big data and online services"
            label="Description"
            rows="5"
            type="textarea"
            control={control}
          />

          <RegistrationRequestField
            id="institutionHomeUrl"
            description="A valid URL for the homepage of the institution."
            example={<a href="https://www.ebi.ac.uk/" target="_blank">https://www.ebi.ac.uk/</a>}
            label="Home URL"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="institutionLocation"
            description="The home country of the institution."
            label="Location"
            optionlabelfield="countryName"
            optionsfield="locations"
            options={props.locationList}
            type="select"
            control={control}
          />
        </div>
      </div>

      <div className="card mb-3">
        <div className="card-header">
          <h2 className="mb-2"><i className="icon icon-common icon-cube"/> Provider details</h2>
          <p>
            This section collects information related to the first provider being registered in this new namespace.
          </p>
        </div>

        <div className="card-body">
          <div className="row mb-3">
            <label
              className="col-lg-3 col-form-label form-control-label form-label"
              htmlFor="institutionIsProvider"
            >
              Copy Institution details
            </label>
            <div className="col-sm-12 col-md-10 col-lg-9">
              <input
                id="institutionIsProvider"
                defaultChecked={institutionIsProvider}
                onChange={handleInstitutionIsProviderChange(setInstitutionIsProvider, methods)}
                type="checkbox"
              />
              <label
                className="form-text text-muted d-inline ms-2"
                id="institutionIsProvider-helpblock"
                htmlFor="institutionIsProvider"
              >
                Tick this box to copy the details provided for the owning institution of the provider. Please,
                keep in mind that 'URL Pattern' and 'Provider code' will need to be filled after the autofill.
              </label>
            </div>
          </div>

          <RegistrationRequestField
            id="providerName"
            description="The name of the provider."
            readonly={institutionIsProvider}
            example="ChEBI (Chemical Entities of Biological Interest)"
            label="Name"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="providerDescription"
            description="Short description of the provider in one or multiple sentences."
            readonly={institutionIsProvider}
            example="ChEBI (Chemical Entities of Biological Interest) at EMBL-EBI"
            label="Description"
            rows="5"
            type="textarea"
            control={control}
          />

          <RegistrationRequestField
            id="providerHomeUrl"
            description="URL for a home page that describes the role of the provider in the current namespace."
            readonly={institutionIsProvider}
            example="https://www.pdbe.org/"
            label="Home URL"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="providerLocation"
            description="The location from which the provider is offering its services (main location in case of multiple ones)."
            readonly={institutionIsProvider}
            label="Location"
            optionlabelfield="countryName"
            optionsfield="locations"
            options={props.locationList}
            type="select"
            control={control}
          />

          <RegistrationRequestField
            id="providerCode"
            description="
              A unique identifier for the provider within the namespace.
              Something related to the institution name is recommended.
              Only lowercase characters, underscores and dots.
            "
            example="ebi"
            label="Provider code"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="providerUrlPattern"
            description="A URL-like string specifying a rule for resolving this identifier. The rule should
                        contain the key &#34;{$id}&#34;, which acts as a placeholder for the resolution services."
            example="https://www.ebi.ac.uk/pdbe/entry/pdb/{$id}"
            label="URL Pattern"
            type="text"
            control={control}
          />

          <ProtectedUrlFormFields control={control} resetField={resetField} trigger={trigger} />
        </div>
      </div>

      <div className="card mb-3">
        <div className="card-header">
          <h2 className="mb-2"><i className="icon icon-common icon-user"/> Requester details</h2>
          <p>
            Contact details associated with this prefix registration request for identifiers.org curator team to reach out
            if needed, for both resolution of this request and future updates to the namespace information and/ or the
            first registered provider.
          </p>
        </div>

        <div className="card-body">
          <RegistrationRequestField
            id="requesterName"
            description="The full name of the person making this request."
            field="requesterName"
            label="Full name"
            type="text"
            control={control}
          />

          <RegistrationRequestField
            id="requesterEmail"
            description="The email address of the person making this request."
            field="requesterEmail"
            label="Email"
            type="text"
            control={control}
          />
        </div>
      </div>

      <div className="card">
        <div className="card-body">
          <div className="row">
            {formState.isSubmitting ? <center className="col-12"><Spinner noText/></center> :
              <>
                <div className="col-12 col-lg-6 mb-1">
                  <button className="form-control btn btn-primary text-white"
                          type="submit" disabled={!isValid}
                          title={!isValid ? "There are errors on the form" : undefined}>
                    Submit prefix request
                  </button>
                </div>
                <div className="col-sm-12 col-md-6 col-lg-3 mb-1">
                  <SaveFormButton storageKey="idorg-prefix-form" getValues={getValues} />
                </div>
                <div className="col-sm-12 col-md-6 col-lg-3 mb-1">
                  <LoadFormButton storageKey="idorg-prefix-form" reset={reset} trigger={trigger} />
                </div>
              </>
            }
          </div>
        </div>
      </div>
    </form>
  )
}

const mapStateToProps = (state) => ({
  locationList: state.locationList
});

export default connect(mapStateToProps)(PrefixRequestForm);