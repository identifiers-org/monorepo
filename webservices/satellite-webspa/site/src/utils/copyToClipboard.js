import {swalToast} from "./swalDialogs";

export const copyToClipboard = (text, ev) => {
  ev.preventDefault(); ev.stopPropagation();
  navigator.clipboard.writeText(text).then(
      () => {
        swalToast.fire({
          icon: 'success',
          title: 'Copied to clipboard'
        })
      },
      (err) => {
        swalToast.fire({
          icon: 'error',
          title: `Failed to copy to clipboard: ${err}`
        })
      }
  );
}