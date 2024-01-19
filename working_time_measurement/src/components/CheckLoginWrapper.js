import React, { Component, useEffect } from 'react';
import { useNavigate} from 'react-router-dom';
import Cookies from 'js-cookie';

function CheckLoginWrapper (props){
    const navigation = useNavigate();
    const { children } = props; 

    useEffect(() => {
        var url = "http://localhost:8083/token-validation";
        const token = Cookies.get("Token");

        if(token === undefined){
          navigation("/login");
        }

        const requestOptions = {
            method: 'GET',
            headers: { 
                'Authorization': ('Basic ' + token),
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