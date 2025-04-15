import React from "react";
import { Outlet } from "react-router-dom";

import Header from "../components/common/Header";
import Footer from "../components/common/Footer";

export default () => <>
  <Header />
  <div className="container">
    <div className="w-100 pt-5">
      <Outlet />
    </div>
  </div>
  <Footer />
</>


