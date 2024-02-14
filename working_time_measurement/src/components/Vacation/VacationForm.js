import React, { Component } from 'react';
import { Button, CloseButton, Stack } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';

class VacationForm extends Component {
    state = {
        startingDate: this.props.startingDate !== undefined ? this.props.startingDate : ((new Date()).toLocaleDateString("de-de")),
        endDate: (this.props.endDate !== undefined ? this.props.endDate : ((new Date()).toLocaleDateString("de-de"))),
        error: {
            startingDate: false,
            endDate: false,
        }
    }

    submitEntry = (navigation) => {
        if(this.state.error.startingDate){
            return;
        }
        if(this.state.error.endDate){
            return;
        }
        
        var envUrl = process.env.REACT_APP_VACATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8081") + "/vacation";

        var hashedUsername = Cookies.get("Username");
        var token = Cookies.get("Token");

        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token)
            },
            body: JSON.stringify({
                startingDate: this.state.startingDate,
                endDate: this.state.endDate,
            })
        };

        fetch(url, requestOptions)
        .then(res => {
            if (res.status > 199 && res.status < 300){
                navigation("/vacation");
            }
        },
        (error) => {
          console.log(error)
        });
    }

    closeForm = (navigation) => {
        navigation("/vacation");
    }


    setStartingDate = (value) => {
        let state = this.state;        

        state.startingDate = value;
        state = this.searchFormErrors(state)
        this.setState(state);
    }

    setEndDate = (value) => {
        let state = this.state;        

        state.endDate = value;
        state = this.searchFormErrors(state)
        this.setState(state);
    }

    searchFormErrors = (state) => {
        let startingDate = state.startingDate;

        if (!this.isValidDate(startingDate)){
            state.error.startingDate = true;
        }else if(state.endDate < startingDate){
            state.error.startingDate = true;
        }else{
            state.error.startingDate = false;
        }

        let endDate = state.endDate;

        if (!this.isValidDate(endDate)){
            state.error.endDate = true;
        }else if(endDate < state.startingDate){
            state.error.endDate = true;
        }else{
            state.error.endDate = false;
        }

        return state;
    }

    isValidDate = (dateString) => {
        const regex = /^\d{1,2}.\d{1,2}.\d{4}$/;
        if (!regex.test(dateString)) {
          return false;
        }
      
        const parts = dateString.split('.');
        const day = parseInt(parts[0], 10);
        const month = parseInt(parts[1], 10) - 1;
        const year = parseInt(parts[2], 10);
      
        const date = new Date(year, month, day);
        const isValid = date.getDate() === day && date.getMonth() === month && date.getFullYear() === year;
        return isValid;
    }

    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3}>
                    <Stack direction='horizontal'>
                        <div className="page-title">Vacation Entry</div>
                        <CloseFormButton closeFunction={this.closeForm}/>
                    </Stack>
                    <div className="form-subtitle">Starting Date</div>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Date</InputGroup.Text>
                        <Form.Control aria-label="Date" value={this.state.startingDate} onChange={(e) => this.setStartingDate(e.target.value)} required isInvalid={this.state.error.startingDate}/>
                    </InputGroup>
                    <div className="form-subtitle">End Date</div>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Date</InputGroup.Text>
                        <Form.Control aria-label="Date" value={this.state.endDate} onChange={(e) => this.setEndDate(e.target.value)} required isInvalid={this.state.error.endDate}/>
                    </InputGroup>
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
 
export default VacationForm;