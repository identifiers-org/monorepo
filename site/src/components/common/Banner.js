// Utils.
//import { swalBanner, swalBannerMobile } from './utils/swalDialogs';

  // // Show beta banner.
  // if (store.getState().config.showBetaBanner) {
  //   const betaBannerMQ = window.matchMedia( "(max-width: 1024px)" );
  //   if (betaBannerMQ.matches) {
  //     swalBannerMobile.fire({
  //       html: `
  //         <i class="icon icon-common icon-info size-300"></i>
  //         <div class="mt-2">
  //           Welcome to the new Identifiers.org! If you are an user from the old platform, and want to know how to migrate to the new
  //           API, check out the <a class="text-primary" href="https://docs.identifiers.org/articles/migration.html">migration guide</a>.
  //           The old Identifiers.org will still be available at <a class="text-primary"
  //           href="https://ebi.identifiers.org">https://ebi.identifiers.org</a> until end of year. Please, report any missing features and bugs in the
  //           <a class="text-primary" href="https://github.com/identifiers-org/identifiers-org.github.io/issues/new">feedback page</a>.
  //         </div>
  //       `
  //     });
  //   }
  //   else {
  //     swalBanner.fire({
  //       html: `
  //         <div class="row">
  //           <div class="col col-sm-12 col-md-2 align-self-center">
  //             <i class="icon icon-common icon-info size-300"></i>
  //           </div>
  //           <div class="col col-sm-12 col-md-10 beta-banner__text">
  //             Welcome to the new Identifiers.org! If you are an user from the old platform, and want to know how to migrate to the new
  //             API, check out the <a class="text-primary" href="https://docs.identifiers.org/articles/migration.html">migration guide</a>.
  //             The old Identifiers.org will still be available at <a class="text-primary"
  //             href="https://ebi.identifiers.org">https://ebi.identifiers.org</a> until end of year. Please, report any missing features and bugs in the
  //             <a class="text-primary" href="https://github.com/identifiers-org/identifiers-org.github.io/issues/new">feedback page</a>.
  //           </div>
  //         </div>
  //       `
  //     });
  //   }
  // }