import React from "react"
import Search from "./search-bar/Search"

export default () => (
  <div className="container my-3 py-1 px-1 rounded bg-lightgray">
    <h4 className="mt-3 p-3">
      <i className="icon icon-common icon-search mr-2"></i>
      Resolve a Compact Identifier
    </h4>
    <Search onButtonClick={console.log}
          buttonCaption="Resolve"
          placeholderCaption="Enter an identifier to resolve" />
  </div>
  
)