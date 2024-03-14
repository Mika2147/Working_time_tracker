import React, { Component, useEffect, useState } from 'react';
import { useNavigate} from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';
import { useMsal } from '@azure/msal-react';
import { msalConfig } from "../Config";



function CheckLoginWrapper (props){
    const navigation = useNavigate();
    const { children } = props; 
    const { roles } = props;
    const {instance} = useMsal();
    var [isAuthorized, setIsAuthorized] = useState(false);
    var [message, setMessage] = useState("");

    const onLoad = async () => {
      const currentAccount = instance.getActiveAccount();
      if (currentAccount){
        if(currentAccount.tenantId = msalConfig.auth.tenantId){
          const idTokenClaims = currentAccount.idTokenClaims;
          if (idTokenClaims && idTokenClaims.aud == msalConfig.auth.clientId && idTokenClaims["roles"]){
            const intersection = roles.filter((role) => idTokenClaims["roles"].includes(role));
            if(intersection.length > 0){
              setIsAuthorized(true);
            }else{
              setMessage("You don't have the required role to view this page. Please contact site administrator.");
            }
          }else{
            setMessage("The application you authorized with cannot access this page. Please contact site administrator.");
          }
        }else{
          setMessage("Your organization does not have access this content.");
        }
      }else{
        setMessage("No user logged in.");
      }
    };
    
    useEffect(() => {
        onLoad();
    });
    
    return (<React.Fragment>{isAuthorized ? <React.Fragment>{children}</React.Fragment> :  <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "90vh" }}>
    <h4>{message}</h4>
  </div> }</React.Fragment>)
    
}

export default CheckLoginWrapper;