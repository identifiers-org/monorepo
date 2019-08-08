import React from 'react';
import { connect } from 'react-redux'

import Swal from 'sweetalert2';

import { config } from '../../config/Config';
import RequestField from '../common/RegistrationRequestField';
import { setValidation, reset, setRegistrationRequestFieldField } from '../../actions/RegistrationRequestField';
import PageTitle from '../common/PageTitle';


/* TODO: This class has to be completely refactored.
 *
 *  - Use validation logic implemented in prefix registration request field reducer
 *    ( Take care about validation requirements ).
 *  - Take UI state to that reducer.
 *  - Streamline and reduce size if possible.
 */
class PrefixRegistrationRequestPage extends React.Component  {
  constructor(props) {
    super(props);

    this.state = {
      institutionIsProvider: false,
      valid: false,
      invalidFields: [],
      fields: [
        'name', 'description', 'requestedPrefix', 'sampleId', 'idRegexPattern', 'supportingReferences', 'additionalInformation',
        'institutionName', 'institutionDescription', 'institutionHomeUrl', 'institutionLocation',
        'providerName', 'providerDescription', 'providerCode', 'providerHomeUrl', 'providerUrlPattern', 'providerLocation',
        'requesterName', 'requesterEmail'
      ],
      optionalFields: ['supportingReferences', 'additionalInformation'],
      speacialPayloads: [
        {requester: this.createRequesterPayload}
      ],
      submitted: false,
      result: undefined
    }

    this.initForm();
  }


  //
  // Form init method. Configures validation status of fields.
  //
  initForm = () => {
    // Fields that should be NOT validated right away.
    this.props.setValidation('sampleId', false);

    // Fields that should never be validated.
    this.props.setValidation('supportingReferences', false);
    this.props.setValidation('additionalInformation', false);
  }

  //
  // Form update method.
  //
  updateForm = (newProps) => {
    // Do not update submitted forms.
    if (this.submitted) {
      return;
    }

    // Validate idRegenPattern if it is not empty if sampleId has changed.
    if (!this.fieldIsEmpty('idRegexPattern', newProps) && this.fieldHasChanged('sampleId', newProps)) {
      this.props.validate('idRegexPattern');
    }

    // Validate sampleId if it is not empty and providerUrlPattern has changed.
    if (!this.fieldIsEmpty('sampleId', newProps) && this.fieldHasChanged('providerUrlPattern', newProps)) {
      this.props.validate('sampleId');
    }

    // Enable validation of sampleId only when providerUrlPattern is not empty.
    this.changeValidity('sampleId', !this.fieldIsEmpty('providerUrlPattern', newProps));

    // Propagate changes and validate for disabled provider fields if institutionIsProvider.
    if (this.state.institutionIsProvider && this.fieldHasChanged('institutionName', newProps)) {
      this.props.setValue('providerName', newProps.institutionName.value);
      if (newProps.institutionName.value !== '') {
        this.props.validate('providerName');
      }
    }

    if (this.state.institutionIsProvider && this.fieldHasChanged('institutionDescription', newProps)) {
      this.props.setValue('providerDescription', newProps.institutionDescription.value);
      if (newProps.institutionDescription.value !== '') {
        this.props.validate('providerDescription');
      }
    }

    if (this.state.institutionIsProvider && this.fieldHasChanged('institutionHomeUrl', newProps)) {
      this.props.setValue('providerHomeUrl', newProps.institutionHomeUrl.value);
      if (newProps.institutionHomeUrl.value !== '') {
        this.props.validate('providerHomeUrl');
      }
    }

    if (this.state.institutionIsProvider && this.fieldHasChanged('institutionLocation', newProps)) {
      this.props.setValue('providerLocation', newProps.institutionLocation.value);
      if (newProps.institutionLocation.value !== '') {
        this.props.validate('providerLocation');
      }
    }

    // Calculate validity of the whole form.
    const invalidFields = this.getInvalidFields();
    const valid = invalidFields.length === 0;

    if (valid !== this.state.valid || invalidFields !== this.state.invalidFields) {
      this.setState({valid, invalidFields});
    }
  }

  // Form update helpers: checks if a field has changed / is empty.
  fieldHasChanged = (field, newProps) => this.props[field].value !== newProps[field].value;
  fieldIsEmpty = (field, newProps) => this.props[field].value.length === 0 && newProps[field].value.length === 0;
  changeValidity = (field, validity) => this.props[field].shouldValidate !== validity ? this.props.setValidation(field, validity) : undefined;


  // Form update hook. Will update anytime new props are received.
  componentWillReceiveProps = (newProps) => {
    this.updateForm(newProps);
  }

  //
  // Form validity checker. Will check required fields validity value, and return those that are invalid.
  //
  getInvalidFields() {
    const fieldsToCheck = this.state.fields.filter(f => !this.state.optionalFields.includes(f));

    return fieldsToCheck.filter(f => this.props[f].valid !== true);
  }


  // Institution is provider check handler. Will copy values from institution to provider fields, enable validation and
  // request a validation.
  handleInstitutionIsProvider = (event) => {
    const institutionIsProvider = event.target.checked;

    this.setState({institutionIsProvider});

    if (institutionIsProvider) {
      this.props.setValue('providerName', this.props.institutionName.value);
      this.props.setValidation('providerName', true);
      this.props.validate('providerName');
      this.props.setValue('providerDescription', this.props.institutionDescription.value);
      this.props.setValidation('providerDescription', true);
      this.props.validate('providerDescription');
      this.props.setValue('providerHomeUrl', this.props.institutionHomeUrl.value);
      this.props.setValidation('providerHomeUrl', true);
      this.props.validate('providerHomeUrl');
      this.props.setValue('providerLocation', this.props.institutionLocation.value);
      this.props.setValidation('providerLocation', true);
      this.props.validate('providerLocation');
    }
  }

  
  // TODO: This should be an action.
  // Handle submit of the form. Supposedly, all fields are valid, as validators would disable this otherwise.
  // But still, some error cases must be treated.
  handleSubmit = () => {
    this.setState({submitted: true}, async () => {
      let body = {
        apiVersion: config.apiVersion,
        payload: this.state.fields.reduce((o, f) => {
          o[f] = this.props[f].value;
          return o;
        }, {})
      };

      // Fix for location not using hateoas link.
      body.payload.institutionLocation = body.payload.institutionLocation.split('/').pop();
      body.payload.providerLocation = body.payload.providerLocation.split('/').pop();

      // Add special payloads.
      this.state.speacialPayloads.forEach(sp => {
        Object.keys(sp).forEach(k => {
          body.payload = {
            ...body.payload,
            ...sp[k]()
          };
        })
      });

      const init = {
        method: 'POST',
        headers: {'content-type': 'application/json'},
        body: JSON.stringify(body)
      };

      // Make request and update the store.
      const requestUrl = `${config.registryApi}/${config.prefixRequestEndpoint}`;
      const response = await fetch(requestUrl, init);
      const responseStatusCode = response.status;
      const json = await response.json();
      const res = { valid: responseStatusCode === 200, errorMessage: json.errorMessage };

      if (res.valid) {
        // Scroll to top.
        window.scrollTo(0, 0);

        // Empty form.
        this.state.fields.forEach(id => {
          this.props.reset(id);
        });

        this.setState({ valid: false, submitted: false });

        await Swal.fire({
          title: 'Prefix registration request sent',
          text: 'Thank you. We will contact you shortly with more information about your request.',
          type: 'success'
        });

        this.props.history.push('/');
      }
      else {
        this.setState({ submitted: false });
        Swal.fire({
          title: 'Error',
          text: 'Please, try again later. The form contents will be saved until you navigate away from identifiers.org.',
          type: 'error'
        });
      }
    });
  }

  // Custom requester payload creation (for validation and submittal).
  createRequesterPayload = () => {
    return {
      requester: {
        name: this.props.requesterName.value,
        email: this.props.requesterEmail.value
      }
    };
  }


  render() {
    const validationUrlBase = `${config.registryApi}/${config.prefixRegistrationRequestValidationEndpoint}/`;
    const { institutionIsProvider, valid, invalidFields } = this.state;
    const { locationList } = this.props;

    return (
      <>
        <PageTitle
          icon="icon-list"
          title="Request prefix form"
          description="Please complete this form to register an identifier prefix that can be recognized by the
                       meta-resolver at identifiers.org. Completing all fields will enable a swift processing of
                       your request."
        />

        <div className="container py-3">
          <div className="row">
            <div className="mx-auto col-sm-12 col-lg-10">
              <div className="form" role="form" autoComplete="off">
                <div className="card mb-3">
                  <div className="card-header">
                    <h2 className="mb-3"><i className="icon icon-common icon-leaf" /> Prefix details</h2>
                    <p className="text-muted">
                      The prefix is a label that identifies the set of data that is being provided in a
                      resource. <strong>Examples:</strong> <span className="text-dark">pdb</span>,
                      <span className="text-dark"> uniprot </span> or <span className="text-dark">kegg.genes</span>.
                    </p>
                  </div>

                  <div className="card-body">
                    <RequestField
                      id="name"
                      description="The name of the resource."
                      example="Protein Data Bank"
                      formsection="Prefix details"
                      label="Resource name"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateName'}
                    />

                    <RequestField
                      id="description"
                      description="Short description of the resource in one or multiple sentences."
                      example="The Protein Data Bank is the single worldwide archive of structural
                        data of biological macromolecules"
                      formsection="Prefix details"
                      label="Description"
                      registrationType="PREFIX"
                      required={true}
                      rows="5"
                      type="textarea"
                      validationurl={validationUrlBase + 'validateDescription'}
                    />

                    <RequestField
                      id="requestedPrefix"
                      description="Character string meant to precede the colon in resolved identifiers. No spaces or
                        punctuation, only lowercase alphanumerical characters, underscores and dots."
                      example="pdb"
                      formsection="Prefix details"
                      label="Requested prefix"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateRequestedPrefix'}
                    />

                    <RequestField
                      id="sampleId"
                      description="An example local identifier."
                      example="2gc4"
                      formsection="Prefix details"
                      label="Sample Id"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationfields={{providerUrlPattern: this.props.providerUrlPattern.value}}
                      validationtooltip={<span>Make sure you wrote <strong>http</strong> or <strong>https</strong> correctly in the <a href="#providerUrlPattern">provider URL pattern</a>.</span>}
                      validationurl={validationUrlBase + 'validateSampleId'}
                    />

                    <RequestField
                      id="idRegexPattern"
                      description="A regular expression specifying the form of string expected for this identifier."
                      example="^[0-9][A-Za-z0-9]{3}$"
                      formsection="Prefix details"
                      label="Regex pattern"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationfields={{sampleId: this.props.sampleId.value}}
                      validationurl={validationUrlBase + 'validateIdRegexPattern'}
                    />

                    <RequestField
                      id="supportingReferences"
                      description="Supporting references (URLs, citations), if any, to published work describing the resource.
                        Enter one per line."
                      example="https://doi.org/10.1093/nar/28.1.235"
                      formsection="Prefix details"
                      label="Supporting references"
                      registrationType="PREFIX"
                      required={false}
                      rows="5"
                      splitByLines={true}
                      type="textarea"
                    />

                    <RequestField
                      id="additionalInformation"
                      description="Anything else you wish to tell or ask us."
                      formsection="Prefix details"
                      label="Additional information"
                      registrationType="PREFIX"
                      required={false}
                      rows="5"
                      type="textarea"
                    />
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header">
                    <h2 className="mb-2"><i className="icon icon-common icon-sitemap" /> Institution details</h2>
                    <p>
                      The resource&#39;s owner institution or organization, who is in charge of creating,
                      developing and maintaining a resource. Examples are EMBL-EBI, Kyoto University
                      or NCBI.
                    </p>
                  </div>

                  <div className="card-body">
                    <RequestField
                      id="institutionName"
                      description="The name of the organization in charge of the resource."
                      formsection="Institution details"
                      example="European Bioinformatics Institute, Hinxton, Cambridge, UK"
                      label="Name"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateInstitutionName'}
                    />

                    <RequestField
                      id="institutionDescription"
                      description="Short description of the institution in one or multiple sentences."
                      example="The European Bioinformatics Institute (EMBL-EBI) is the part of EMBL
                        dedicated to big data and online services"
                      formsection="Institution details"
                      label="Description"
                      registrationType="PREFIX"
                      required={true}
                      rows="5"
                      type="textarea"
                      validationurl={validationUrlBase + 'validateInstitutionDescription'}
                    />

                    <RequestField
                      id="institutionHomeUrl"
                      description="A valid URL for the homepage of the institution or organization."
                      example="https://www.ebi.ac.uk/"
                      formsection="Institution details"
                      label="Home URL"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateInstitutionHomeUrl'}
                    />

                    <RequestField
                      id="institutionLocation"
                      description="The home country of the institution or organization."
                      formsection="Institution details"
                      label="Location"
                      optionlabelfield="countryName"
                      optionsfield="locations"
                      options={locationList}
                      registrationType="PREFIX"
                      required={true}
                      type="select"
                      validationurl={validationUrlBase + 'validateInstitutionLocation'}
                    />
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header">
                    <h2 className="mb-2"><i className="icon icon-common icon-cube" /> Provider details</h2>
                    <p>
                      The <span className="text-italic">provider</span> is the institution or organization
                      in charge of providing a resource. There can be more than one provider, and
                      it can be the same or different than the institution in charge of the resource.
                    </p>
                  </div>

                  <div className="card-body">
                    <div className="form-group row">
                      <label
                        className="col-lg-3 col-form-label form-control-label"
                        htmlFor="institutionIsProvider"
                      >
                        Self provided
                      </label>
                      <div className="col-lg-9">
                        <div className="form-check">
                          <input
                            id="institutionIsProvider"
                            className="form-check-input"
                            defaultChecked={this.state.institutionIsProvider}
                            onChange={this.handleInstitutionIsProvider}
                            type="checkbox"
                          />
                          <label
                            id="institutionIsProvider-helpblock"
                            className="form-text"
                            htmlFor="institutionIsProvider"
                          >
                            Tick this checkbox if the owner institution is also in charge of providing the resource
                            online. In that case, you will only have to fill in the resource&#39;s URL.
                          </label>
                        </div>
                      </div>
                    </div>

                    <RequestField
                      id="providerName"
                      description="The name of the organization providing the resource."
                      disabled={institutionIsProvider}
                      example="European Bioinformatics Institute, Hinxton, Cambridge, UK"
                      formsection="Provider details"
                      label="Name"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateProviderName'}
                    />

                    <RequestField
                      id="providerDescription"
                      description="Short description of the provider in one or multiple sentences."
                      disabled={institutionIsProvider}
                      example="The European Bioinformatics Institute (EMBL-EBI) is the part of EMBL dedicated to big
                        data and online services"
                      formsection="Provider details"
                      label="Description"
                      registrationType="PREFIX"
                      required={true}
                      rows="5"
                      type="textarea"
                      validationurl={validationUrlBase + 'validateProviderDescription'}
                    />

                    <RequestField
                      id="providerHomeUrl"
                      description="A valid URL describing the resource."
                      disabled={institutionIsProvider}
                      example="http://www.pdbe.org/"
                      formsection="Provider details"
                      label="Home URL"
                      registrationType="PREFIX"
                      required={true}
                      rows="5"
                      type="text"
                      validationurl={validationUrlBase + 'validateProviderHomeUrl'}
                    />

                    <RequestField
                      id="providerCode"
                      description="Character string meant to optionally designate this provider in identifiers. No
                        spaces or punctuation, only lowercase alphanumerical characters, underscores and dots."
                      example="pdb"
                      formsection="Provider details"
                      label="Provider code"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateProviderCode'}
                    />

                    <RequestField
                      id="providerUrlPattern"
                      description="A URL-like string specifying a rule for resolving this identifier. The rule should
                        contain the placeholder &#34;{$id}&#34;, which is a placeholder for the local identifier."
                      example="http://www.ebi.ac.uk/pdbe/entry/pdb/{$id}"
                      formsection="Provider details"
                      label="URL Pattern"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validationurl={validationUrlBase + 'validateProviderUrlPattern'}
                    />

                    <RequestField
                      id="providerLocation"
                      description="The home country of the provider institution or organization."
                      disabled={institutionIsProvider}
                      formsection="Provider details"
                      label="Location"
                      optionlabelfield="countryName"
                      optionsfield="locations"
                      options={locationList}
                      registrationType="PREFIX"
                      required={true}
                      type="select"
                      validationurl={validationUrlBase + 'validateProviderLocation'}
                    />
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header">
                    <h2 className="mb-2"><i className="icon icon-common icon-user" /> Requester details</h2>
                    <p>
                      The requester details are required to contact the person who is creating
                      this request if further information is required.
                    </p>
                  </div>

                  <div className="card-body">
                    <RequestField
                      id="requesterName"
                      description="The full name of the person making this request."
                      field="requesterName"
                      formsection="Requester details"
                      label="Full name"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validation={this.standardValidator}
                      validationfields={['requesterEmail']}
                      validationurl={validationUrlBase + 'validateRequesterName'}
                      customvalidationpayload={this.createRequesterPayload}
                    />

                    <RequestField
                      id="requesterEmail"
                      description="The email address of the person making this request."
                      field="requesterEmail"
                      formsection="Requester details"
                      label="Email"
                      registrationType="PREFIX"
                      required={true}
                      type="text"
                      validation={this.standardValidator}
                      validationfields={['requesterName']}
                      validationurl={validationUrlBase + 'validateRequesterEmail'}
                      customvalidationpayload={this.createRequesterPayload}
                      defaultValue="asdf"
                    />
                  </div>
                </div>

                <div className="card m-5">
                  <div className="card-header">
                    <h2 className="mb-2"><i className="icon icon-common icon-tasks" /> Request form status</h2>
                  </div>
                  <div className="card-body">
                    <>
                      {
                        valid ? (
                          <div className="row">
                            <div className="col">
                              <div className="d-flex align-items-center">
                                <h1 className="text-success mb-0 mr-1"><i className="icon icon-common icon-check" /></h1>
                                <span className="font-weight-bold">Request is complete and ready to send.</span>
                              </div>
                            </div>
                          </div>
                        ) : (
                          <>
                            <div className="row">
                              <div className="col">
                                <div className="d-flex align-items-center">
                                  <h1 className="text-danger mb-0 mr-1"><i className="icon icon-common icon-times" /></h1>
                                  <span className="font-weight-bold">Request contains errors or empty required fields.</span>
                                </div>
                              </div>
                            </div>
                            <div className="row">
                              <div className="col mx-5 mb-3">
                                <p className="mb-0">The following required fields are empty or contain errors:</p>
                                <span className="text-muted"><small>please fill or correct them before submitting the request</small></span>
                              </div>
                            </div>
                            <div className="row">
                              <div className="col mx-5">
                                <div className="list-group mb-3 px-5">
                                  {
                                    invalidFields.map((field, i) => (
                                      <a
                                        className="list-group-item list-group-item-action list-group-item-danger py-1"
                                        href={`#${field}`}
                                        key={`invalidField-${field}-${i}`}
                                      >
                                        {
                                          this.props[field].label.map((label, i) => (
                                            <span key={`labelcontainer-${label}-${i}`}>
                                              <span key={`label-${label}-${i}`}>{label}</span>
                                              {
                                                i < this.props[field].label.length - 1 &&
                                                <i key={`label-${label}-${i}-arrow`} className="icon icon-common icon-caret-right mx-1" />
                                              }
                                            </span>
                                          ))
                                        }
                                      </a>
                                    ))
                                  }
                                </div>
                              </div>
                            </div>
                          </>
                        )
                      }
                      <button
                        className="form-control btn btn-primary"
                        disabled={!this.state.valid && !this.state.sent}
                        onClick={this.handleSubmit}
                      >
                        Submit prefix request
                      </button>
                    </>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </>
    );
  }
}

// Mapping
const mapStateToProps = (state) => ({
  name: state.prefixRegistrationRequestForm.name,
  description: state.prefixRegistrationRequestForm.description,
  requestedPrefix: state.prefixRegistrationRequestForm.requestedPrefix,
  sampleId: state.prefixRegistrationRequestForm.sampleId,
  idRegexPattern: state.prefixRegistrationRequestForm.idRegexPattern,
  supportingReferences: state.prefixRegistrationRequestForm.supportingReferences,
  additionalInformation: state.prefixRegistrationRequestForm.additionalInformation,
  institutionName: state.prefixRegistrationRequestForm.institutionName,
  institutionDescription: state.prefixRegistrationRequestForm.institutionDescription,
  institutionHomeUrl: state.prefixRegistrationRequestForm.institutionHomeUrl,
  institutionLocation: state.prefixRegistrationRequestForm.institutionLocation,
  institutionIsProvider: state.prefixRegistrationRequestForm.institutionIsProvider,
  providerName: state.prefixRegistrationRequestForm.providerName,
  providerDescription: state.prefixRegistrationRequestForm.providerDescription,
  providerCode: state.prefixRegistrationRequestForm.providerCode,
  providerHomeUrl: state.prefixRegistrationRequestForm.providerHomeUrl,
  providerUrlPattern: state.prefixRegistrationRequestForm.providerUrlPattern,
  providerLocation: state.prefixRegistrationRequestForm.providerLocation,
  requesterName: state.prefixRegistrationRequestForm.requesterName,
  requesterEmail: state.prefixRegistrationRequestForm.requesterEmail,
  locationList: state.locationList
});

const mapDispatchToProps = (dispatch) => ({
  setValue: (id, value) => dispatch(setRegistrationRequestFieldField('PREFIX', id, 'value', value)),
  validate: (id) => dispatch(setRegistrationRequestFieldField('PREFIX', id, 'requestedValidate', true)),
  setValidation: (id, validation) => dispatch(setValidation('PREFIX', id, validation)),
  reset: (id) => dispatch(reset('PREFIX', id))
});

export default connect (mapStateToProps, mapDispatchToProps)(PrefixRegistrationRequestPage);
