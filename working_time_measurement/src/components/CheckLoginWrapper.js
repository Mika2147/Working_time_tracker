import React, { Component, useEffect } from 'react';
import { useNavigate} from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';


function CheckLoginWrapper (props){
    const navigation = useNavigate();
    const { children } = props; 

    useEffect(() => {
        var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8083") + "/token-validation";
        var hashedUsername = Cookies.get("Username");
        const token = Cookies.get("Token");

        if(token === undefined){
          navigation("/login");
        }

        const requestOptions = {
            method: 'GET',
            headers: { 
                'Authorization': ('Basic ' + hashedUsername + ":" + token),
            },
        };

        fetch(url, requestOptions)
        .then(res => res.status)
        .then(
        (result) => {
          if (result !== 200){
            navigation("/login");
          }

        },
        (error) => {
            navigation("/login");  
        }
      )
    });
    
    return (<React.Fragment>{children}</React.Fragment>)
    
}

export default CheckLoginWrapper;