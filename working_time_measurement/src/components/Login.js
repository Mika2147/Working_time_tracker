import React, { Component, useState } from 'react';
import { Button, Stack } from 'react-bootstrap';
import { useNavigate, useSearchParams} from 'react-router-dom';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { md5 } from 'js-md5';
import Cookies from 'js-cookie';

function Login(props){
    const navigation = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const login = () => {
        var url = "http://localhost:8083/login";

        var hashedUsername = md5(username);
        var hashedPassword = md5(password);
        
        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + hashedPassword),
            },
        };

        fetch(url, requestOptions)
        .then(res => res.text())
        .then(
        (result) => {
            debugger;
            if (result !== "") {
                Cookies.set("Token", result)
                Cookies.set("Username", username)
                navigation("/time-measurement");
            }
        },
        (error) => {
          console.log(error);
        }
      )
    }

    return(
        <React.Fragment>
            <div className='login-container'>
                <Stack direction="vertical">
                    <InputGroup className="mb-3">
                        <InputGroup.Text id="login_username_control">Username</InputGroup.Text>
                        <Form.Control
                            placeholder="Username"
                            aria-label="Username"
                            aria-describedby="login_username_control"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                        />
                    </InputGroup>
                    <InputGroup className="mb-3">
                        <InputGroup.Text id="login_password_control">Password</InputGroup.Text>
                        <Form.Control
                            type="password"
                            placeholder="Password"
                            aria-label="Password"
                            aria-describedby="login_password_control"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                    </InputGroup>
                    <Button onClick={() => {
                        login();
                        }
                    }>Login</Button>
                </Stack>
            </div>
        </React.Fragment>
    )
}
 
export default Login;