import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { useHistory, useNavigate } from 'react-router-dom';

class TimeEnteringForm extends Component {
    state = { 
        date: "05.01.2024",
        startingHour: "8",
        startingMinute: "00",
        endHour: "17",
        endMinute: "00",
        breakDuration: "60",
        error: {
            date: false,
            startingHour: false,
            startingMinute: false,
            endHour: false,
            endMinute: false,
            breakDuration: false
        }
     }
     
    submitEntry = (navigation) => {
        if(this.state.error.date){
            return;
        }
        if(this.state.error.startingHour === true){
            return;
        }
        if(this.state.error.startingMinute === true){
            return;
        }
        if(this.state.error.endHour === true){
            return;
        }
        if(this.state.error.endMinute === true){
            return;
        }
        if(this.state.error.breakDuration === true){
            return;
        }

        var url = "http://localhost:8080/time";
        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                date: this.state.date,
                startingHour: this.state.startingHour,
                startingMinute: this.state.startingMinute,
                endHour: this.state.endHour,
                endMinute: this.state.endMinute,
                breakDuration: this.state.breakDuration
            })
        };

        fetch(url, requestOptions)
        .then(res => {
            debugger;
            if (res.status > 199 && res.status < 300){
                navigation("/time-measurement");
            }
        },
        (error) => {
          console.log(error)
        });
    }

    setDate = (date) => {
        let state = this.state;        

        state.date = date;
        state = this.searchFormErrors(state)
        this.setState(state);
    }

    setStartingHour = (startingHour) => {
        let state = this.state;
        
        state.startingHour = startingHour;
        state = this.searchFormErrors(state);
        this.setState(state);
    }

    setStartingMinute = (startingMinute) => {
        let state = this.state;
        
        state.startingMinute = startingMinute;
        state = this.searchFormErrors(state);
        this.setState(state);    
    }

    setEndHour = (endHour) => {
        let state = this.state;
        
        state.endHour = endHour;
        state = this.searchFormErrors(state);
        this.setState(state);
    
    }

    setEndMinute = (endMinute) => {
        let state = this.state;
        
        state.endMinute = endMinute;
        state = this.searchFormErrors(state);
        this.setState(state);
    }

    setBreakDuration = (breakDuration) => {
        let state = this.state;
       
        state.breakDuration = breakDuration;
        state = this.searchFormErrors(state);
        this.setState(state);
    }

    searchFormErrors = (state) => {
        let date = state.date;
        var dateRegex = /^(\d{2}).(\d{2}).(\d{4})$/;

        if (!dateRegex.test(date)){
            state.error.date = true;
        }else if (!this.isValidDate(date)){
            state.error.date = true;
        }else{
            state.error.date = false;
        }

        let startingHour = state.startingHour;
        let hourRegex = /^(\d{1,2})$/;
        
        if(!hourRegex.test(startingHour)){
            state.error.startingHour = true;
        }else if(Number(startingHour) < 0 || Number(startingHour) > 23){
            state.error.startingHour = true
        }else if(Number(state.endHour) < Number(startingHour)){
            state.error.startingHour = true;
        }else{
            state.error.startingHour = false;
        }

        let startingMinute = state.startingMinute;
        let minuteRegex = /^(\d{2})$/;
        
        if(!minuteRegex.test(startingMinute)){
            state.error.startingMinute = true;
        }else if(Number(startingMinute) < 0 || Number(startingMinute) > 59){
            state.error.startingMinute = true
        }else if(Number(state.startingHour) == Number(state.endHour) && Number(startingMinute) > Number(state.endMinute)){
            state.error.startingMinute = true
        }
        else{
            state.error.startingMinute = false;
        }

        let endHour = state.endHour;

        if(!hourRegex.test(endHour)){
            state.error.endHour = true;
        }else if(Number(endHour) < 0 || Number(endHour) > 23){
            state.error.endHour = true
        }else if(Number(endHour) < Number(this.state.startingHour)){
            state.error.endHour = true;
        }else{
            state.error.endHour = false;
        }

        let endMinute = state.endMinute;
        
        if(!minuteRegex.test(endMinute)){
            state.error.endMinute = true;
        }else if(Number(endMinute) < 0 || Number(endMinute) > 59){
            state.error.endMinute = true;
        }else if(Number(state.endHour) == Number(state.startingHour) && Number(endMinute) < Number(state.startingMinute)){
            state.error.endMinute = true;
        }
        else{
            state.error.endMinute = false;
        }

        let breakRegex = /^(\d{2,3})$/;
        let breakDuration = state.breakDuration;

        if(!breakRegex.test(breakDuration)){
            state.error.breakDuration = true;
        }else if(Number(breakDuration) < 0){
            state.error.breakDuration = true;
        }else{
            state.error.breakDuration = false;
        }

        return state;
    }

    isValidDate = (dateString) => {
        const regex = /^\d{2}\.\d{2}\.\d{4}$/;
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
                    <div className="page-title">Time Entry</div>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Date</InputGroup.Text>
                        <Form.Control aria-label="Date" value={this.state.date} onChange={(e) => this.setDate(e.target.value)} required isInvalid={this.state.error.date}/>
                    </InputGroup>
                    <div className="form-subtitle">Starting Time</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Hour</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" value={this.state.startingHour} onChange={(e) => this.setStartingHour(e.target.value)} type='number' required isInvalid={this.state.error.startingHour}/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minute</InputGroup.Text>
                            <Form.Control aria-label="Starting Minute" value={this.state.startingMinute} onChange={(e) => this.setStartingMinute(e.target.value)} type='number' required isInvalid={this.state.error.startingMinute}/>
                        </InputGroup>
                    </Stack>
                    <div className="form-subtitle">End Time</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Hour</InputGroup.Text>
                            <Form.Control aria-label="End Hour" value={this.state.endHour} onChange={(e) => this.setEndHour(e.target.value)} type='number' required isInvalid={this.state.error.endHour}/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minute</InputGroup.Text>
                            <Form.Control aria-label="End Minute" value={this.state.endMinute} onChange={(e) => this.setEndMinute(e.target.value)} type='number' required isInvalid={this.state.error.endMinute}/>
                        </InputGroup>
                    </Stack>
                    <div className="form-subtitle">Breaks</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minutes</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" value={this.state.breakDuration} onChange={(e) => this.setBreakDuration(e.target.value)} type='number' required isInvalid={this.state.error.breakDuration}/>
                        </InputGroup>
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
            debugger;
            props.submitFunction(navigation)
            }
        }>Submit</Button>
    )
}
 
export default TimeEnteringForm;