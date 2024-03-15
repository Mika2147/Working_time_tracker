import React, { Component } from 'react';
import { Button, CloseButton, Form, InputGroup, Stack } from 'react-bootstrap';
import { useNavigate, useSearchParams } from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';
import { Token } from '../../Token';


class UserEditForm extends Component {
    state = {
        id: this.props.id,
        username: this.props.name,
        password: "",
        repeatPassword: "",
        isAdmin: (this.props.isAdmin === "true"),
        error: {
            username: false,
            password: false,
            repeatPassword: false,
            isAdmin: false,
        }
    }

    componentDidMount(){
        console.log(this.state);
    }

    setPassword = (password) => {
        let state = this.state;        

        state.password = password;
        state = this.validateState(state)
        this.setState(state);
    }

    setRepeatPassword = (repeatPassword) => {
        let state = this.state;        

        state.repeatPassword = repeatPassword;
        state = this.validateState(state)
        this.setState(state);
    }

    setIsAdmin = (isAdmin) => {
        let state = this.state;        

        state.isAdmin = isAdmin;
        state = this.validateState(state)
        this.setState(state);
    }

    validateState = (state) => {

        if(state.password == state.repeatPassword){
            state.error.password = false;
        }else{
            state.error.password = true;
        }

        if(state.password == state.repeatPassword){
            state.error.repeatPassword = false;
        }else{
            state.error.repeatPassword = true;
        }

        return state;
    }

    closeForm = (navigation) => {
        navigation("/users");
    }

    submitEntry = async (navigation) => {
        if(this.state.error.password){
            return;
        }
        if(this.state.error.repeatPassword){
            return;
        }
        if(this.state.error.isAdmin){
            return;
        }

        var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8083") + "admin/users/" + this.state.id;

        var hashedUsername = Cookies.get("Username");
        var token = await Token.getToken();

        let password = this.state.password.trim().length > 0 ? md5(this.state.password) : "";

        const requestOptions = {
            method: 'Put',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token)
            },
            body: JSON.stringify({
                id: this.state.id,
                name: this.state.username,
                password: password,
                isadmin: this.state.isAdmin,
            })
        };

        fetch(url, requestOptions)
        .then(res => {
            if (res.status > 199 && res.status < 300){
                navigation("/users");
            }
        },
        (error) => {
          console.log(error)
        });
    }



    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3}>
                    <Stack direction='horizontal'>
                        <div className="page-title">{this.state.id}: {this.state.username}</div>
                        <CloseFormButton closeFunction={this.closeForm}/>
                    </Stack>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Password</InputGroup.Text>
                        <Form.Control aria-label="Password" value={this.state.password} onChange={(e) => this.setPassword(e.target.value)} type='password' required isInvalid={this.state.error.password}/>
                    </InputGroup>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Repeat Password</InputGroup.Text>
                        <Form.Control aria-label="Repeat Password" value={this.state.repeatPassword} onChange={(e) => this.setRepeatPassword(e.target.value)} type='password' required isInvalid={this.state.error.repeatPassword}/>
                    </InputGroup>
                    <Stack direction='horizontal' gap={3}>
                        <div>Admin:</div>
                        <Form.Check inline onChange={(e) => this.setIsAdmin(e.target.value)} checked={this.state.isAdmin}/>
                    </Stack>
                    <SubmitButton submitFunction={this.submitEntry} />
                </Stack>
                
            </div>
        </React.Fragment>);
    }
}

function SubmitButton(props){
    const navigation = useNavigate();
    return(
        <Button onClick={() => {
            props.submitFunction(navigation)
            }
        }>Submit</Button>
    )
}

function CloseFormButton(props){
    const navigation = useNavigate();
    return(
        <CloseButton onClick={() => {
            props.closeFunction(navigation)
            }
        }/>
    )
}

const withQueryParamsHOC = (Component) =>{
    return (props) =>{
        const [searchParams, setSearchParams] = useSearchParams();
    let id = searchParams.get("id");
    let name = searchParams.get("name");
    let isAdmin = searchParams.get("isAdmin");

    return <Component id={id} name={name} isAdmin={isAdmin}/>

    }
}
 
export default withQueryParamsHOC(UserEditForm);