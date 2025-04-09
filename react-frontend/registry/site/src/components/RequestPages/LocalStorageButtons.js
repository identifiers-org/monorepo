import React, {useCallback} from "react";
import {swalError, swalSuccess} from "../../utils/swalDialogs";

export const SaveFormButton = ({storageKey, getValues}) => {
  const saveCurrentValuesToLocalStorage = useCallback(async () => {
    const values = getValues();
    try {
      const jsonStringValues = JSON.stringify(values);
      localStorage.setItem(storageKey, jsonStringValues);
      await swalSuccess.fire({
        title: "Current form values saved successfully",
        text: "Current form values were saved successfully, " +
          "when you wish to continue the session, " +
          "click on the load button bellow",
        icon: "success",
      });
    } catch (error) {
      await swalError.fire({
        icon: "error",
        title: "Error on value save",
        text: error.message
      });
    }
  }, [getValues, storageKey]);

  return (
    <button className="form-control btn btn-secondary"
            type="button" onClick={saveCurrentValuesToLocalStorage}>
      Save contents
    </button>
  )
}

export const LoadFormButton = ({storageKey, reset, trigger}) => {
  const setValuesFromLocalStorage = useCallback(async () => {
    const localStorageValues = localStorage.getItem(storageKey);
    if (localStorageValues) {
      try {
        const parsedLocalStorage = JSON.parse(localStorageValues);
        reset(parsedLocalStorage);
        await trigger()
            .then(() =>
              swalSuccess.fire({
                icon: "success",
                title: "Load successful",
                text: "Your data was successfully loaded"
              })
            )
      } catch (error) {
        await swalError.fire({
          icon: "error",
          title: "Error on value load",
          text: error.message
        })
      }
    } else {
      await swalError.fire({
        icon: "error",
        title: "Error on value load",
        text: "We couldn't find any saved form. Perhaps you didn't save before or you cleared you local data"
      })
    }
  }, [reset, trigger, storageKey]);

  return (
    <button className="form-control btn btn-secondary"
            type="button" onClick={setValuesFromLocalStorage}>
      Load previous save
    </button>
  )
}

