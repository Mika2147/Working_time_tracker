import React, { Component } from 'react';
import * as ReactDOMClient from "react-dom/client"
import "bootstrap/dist/css/bootstrap.min.css"
import App from "./components/App";
import "./index.css"

const root = ReactDOMClient.createRoot(document.getElementById("root"));
root.render(<App/>);