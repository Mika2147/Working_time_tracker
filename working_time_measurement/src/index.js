import React, { Component } from 'react';
import * as ReactDOMClient from "react-dom/client"
import "bootstrap/dist/css/bootstrap.min.css"
import App from "./components/App";
import "./index.css"
import { PublicClientApplication, EventType, EventMessage, AuthenticationResult } from "@azure/msal-browser";
import { msalConfig, protectedResources } from "./Config";

export const msalInstance = new PublicClientApplication(msalConfig);
await msalInstance.initialize();
const accounts = msalInstance.getAllAccounts();
if (accounts.length == 1) {
  const account = accounts[0];
  if (account?.tenantId === msalConfig.auth.tenantId) {
    msalInstance.setActiveAccount(account);
  }
} else if (accounts.length > 1) {
  accounts.forEach((account) => {
    if (account?.tenantId === msalConfig.auth.tenantId) {
      msalInstance.setActiveAccount(account);
    }
  });
}else{
    msalInstance.loginPopup();
}

function loginRedirect() {
    try {
      const loginRequest = {
        scopes: [protectedResources.time.scopes],
      };
      msalInstance.loginRedirect(loginRequest);
    } catch (err) {
      console.log(err);
    }
}

msalInstance.addEventCallback((event) => {
    if (event.eventType === EventType.LOGIN_SUCCESS && event.payload) {
      const payload = event.payload;
      const account = payload.account;
      msalInstance.setActiveAccount(account);
    } else if (event.eventType === EventType.ACQUIRE_TOKEN_FAILURE) {
      if (event.error?.name === "InteractionRequiredAuthError" && window.location.pathname.startsWith(protectedResources.time.path)) {
        loginRedirect();
      } else {
        console.log("ACQUIRE_TOKEN_FAILURE");
      }
    } else if (event.eventType === EventType.LOGIN_FAILURE) {
      if (event.error?.name === "BrowserAuthError" && window.location.pathname.startsWith(protectedResources.time.path)) {
        loginRedirect();
      } else {
        console.log("LOGIN FAILURE");
      }
    } else {
      console.log("Callback finished");
    }
  });
  msalInstance
    .handleRedirectPromise()
    .then(() => {
      if (window.location.pathname.startsWith(protectedResources.time.path)) {
        const account = msalInstance.getActiveAccount();
        if (!account) {
          loginRedirect();
        }
      }
    })
    .catch((err) => {
      console.log(err);
    });


const root = ReactDOMClient.createRoot(document.getElementById("root"));
root.render(<App pca={msalInstance}/>);