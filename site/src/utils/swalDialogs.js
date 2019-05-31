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
});


export const swalError = Swal.mixin({
  customClass: {
    confirmButton: 'btn btn-success mx-2'
  },
  buttonsStyling: false,
  type: 'error'
});

export const swalBanner = Swal.mixin({
  customClass: {
    actions: 'beta-banner__actions',
    confirmButton: 'btn btn-light mx-2',
    content: 'beta-banner__content',
    popup: 'beta-banner',

  },
  buttonsStyling: false,
  confirmButtonText: 'Dismiss',
  toast: true,
  position: 'top',
  timer: 15000
});

export const swalBannerMobile = Swal.mixin({
  customClass: {
    confirmButton: 'btn btn-light mx-2'
  },
  buttonsStyling: false,
  confirmButtonText: 'Dismiss'
});