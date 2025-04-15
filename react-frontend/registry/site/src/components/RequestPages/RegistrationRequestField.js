import React, {memo, useCallback, useRef} from 'react';
import {useController, useFormContext} from "react-hook-form";

const RegistrationRequestField = (props) => {
  const {
    field: {onChange, onBlur, value, name, ref},
    fieldState: {invalid, isTouched, error},
  } = useController({
    name: props.id,
    control: props.control,
    disabled: props.disabled,
  })

  const id = `form-control-${props.id}`;
  const validityClass = isTouched? (invalid ? 'is-invalid' : 'is-valid') : '';
  const helpElementId = `${id}-helpblock`;

  let PreparedAutocompleter;
  if (props.autocompleter) {
    PreparedAutocompleter = React.createElement(
      props.autocompleter, {
        watchedId: props.id,
        setValue: props.autocompleterSetValue
      },
    )
  }

  return (
    <div className="row mb-3">
      <label className="col-md-12 col-md-2 col-lg-3 col-form-label form-control-label form-label" htmlFor={props.id}>
        {props.label}
      </label>
      <div className="col-sm-12 col-md-10 col-lg-9 pt-1">
        {(() => {
          switch (props.type) {
            case "text":
            case "password":
              return (
                <input
                  onChange={onChange}
                  onBlur={onBlur}
                  value={value}
                  name={name}
                  ref={ref}
                  id={props.id}
                  type={props.type}
                  className={`form-control ${validityClass}`}
                  aria-describedby={helpElementId}
                  readOnly={props.readonly}
                  placeholder={props.placeholder}
                />
              )
            case "checkbox":
              return (
                  <input
                      onChange={onChange}
                      onBlur={onBlur}
                      checked={value}
                      value={value}
                      name={name}
                      ref={ref}
                      id={props.id}
                      type={props.type}
                      className={validityClass}
                      aria-describedby={helpElementId}
                      readOnly={props.readonly}
                      placeholder={props.placeholder}
                  />
              )
            case "textarea":
              return (
                <textarea
                  onChange={onChange}
                  onBlur={onBlur}
                  value={value}
                  name={name}
                  ref={ref}
                  id={props.id}
                  className={"form-control textarea " + validityClass}
                  aria-describedby={helpElementId}
                  readOnly={props.readonly}
                  placeholder={props.placeholder}
                  rows={props.rows}
                />
              )
            case "select":
              return (
                <select
                  onChange={onChange}
                  onBlur={onBlur}
                  value={value}
                  name={name}
                  ref={ref}
                  id={props.id}
                  className={"form-control form-select " + validityClass}
                  aria-describedby={helpElementId}
                  disabled={props.readonly}
                >
                  <option value="" disabled>{props.placeholder || 'Select...'}</option>
                  {
                    props.options.map(option => (
                      <option
                        value={option.shortId}
                        key={`option-${option.shortId}`}
                      >
                        {option.label}
                      </option>
                    ))
                  }
                </select>
              )
          }
        })()}
        { props.autocompleter && PreparedAutocompleter }
        <div className={invalid ? 'd-block text-danger' : 'd-none'}>
          <i className="icon icon-common icon-times-circle" /> { error?.message }
        </div>
        <small id={`${id}-helpblock`} className={`form-text my-1 text-muted ${props.type === 'checkbox' && "ms-2"}`}>
          {props.description}
          {props.example && <span> <strong>Example:</strong> <span className="text-dark">{props.example}</span>.</span>}
        </small>
      </div>

    </div>
  );
}

export default RegistrationRequestField;

// export default (props) => {
//   const {
//     register,
//     getFieldState,
//     formState
//   } = useFormContext();
//   const MemoizedRegistrationRequestField = memo(RegistrationRequestField,
//       (prevProps, nextProps) => {
//         const prevIsDirty = getFieldState(prevProps.id, prevProps.formState).isDirty;
//         const nextIsDirty = getFieldState(prevProps.id, nextProps.formState).isDirty;
//         return prevIsDirty === nextIsDirty
//       }
//   );
//   return <MemoizedRegistrationRequestField
//       {...props}
//       register={register}
//       getFieldState={getFieldState}
//       formState={formState}
//   />
// }