import Swal from "sweetalert2";


export const swalConfirmation = Swal.mixin({
  customClass: {
    cancelButton: 'btn btn-danger mx-2',
    confirmButton: 'btn btn-success mx-2'
  },
  buttonsStyling: false,
  cancelButtonText: 'Cancel',
  confirmButtonText: 'Confirm',
  showCancelButton: true,
  type: 'question'
});


export const swalSuccess = Swal.mixin({
  customClass: {
    confirmButton: 'btn btn-success mx-2'
  },
  buttonsStyling: false,
  type: 'success'
})


export const swalError = Swal.mixin({
  customClass: {
    confirmButton: 'btn btn-success mx-2'
  },
  buttonsStyling: false,
  type: 'error'
})