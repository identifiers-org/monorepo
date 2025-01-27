import Swal from "sweetalert2";
import withReactContent from 'sweetalert2-react-content';


export const swalConfirmation = Swal.mixin({
  customClass: {
    cancelButton: 'btn btn-danger mx-2',
    confirmButton: 'btn btn-success mx-2'
  },
  backdrop: true,
  buttonsStyling: false,
  cancelButtonText: 'Cancel',
  confirmButtonText: 'Confirm',
  showCancelButton: true,
  type: 'question'
});

export const namespacesUsingInstitutionsModal = withReactContent(Swal.mixin({
  backdrop: true,
  buttonsStyling: false,
  confirmButtonText: 'Open those namespaces',
  cancelButtonText: 'Ok',
  customClass: {
    confirmButton: 'btn btn-success mx-2',
    cancelButton: 'btn btn-primary mx-2'
  },
  showCancelButton: true,
  title: 'Error deleting institution',
  type: 'error'
}));


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
  animation: false,
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
  animation: false,
  customClass: {
    confirmButton: 'btn btn-light mx-2'
  },
  buttonsStyling: false,
  confirmButtonText: 'Dismiss'
});

const toast = (type, title, html, position, timer) => Swal.mixin({
  type,
  title,
  html,
  toast: true,
  position,
  showConfirmButton: false,
  timer: timer || 3000
}).fire();

export const successToast = (title, html, timer) => toast('success', title, html, 'top-end', timer);
export const failureToast = (title, html, timer) => toast('error', title, html, 'top-end', timer);
export const infoToast = (title, html, timer) => toast('info', title, html, 'top-end', timer);
