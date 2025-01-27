import React from "react";
import { createRoot } from "react-dom/client";
import Popup from "./popup.tsx";
// import "webextension-polyfill";

const container = document.getElementById("mainContainer");
const root = createRoot(container);
root.render(<Popup />);
