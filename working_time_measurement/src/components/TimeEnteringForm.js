import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { useHistory, useNavigate } from 'react-router-dom';

class TimeEnteringForm extends Component {
    state = { 
        date: "05-01-2024",
        startingHour: "8",
        startingMinute: "00",
        endHour: "17",
        endMinute: "00",
        breakDuration: "60"
     }
     
    submitEntry = (navigation) => {
        console.log(this.state)
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
        
        // TODO: Validation here
        if(true){
            state.date = date;
            this.setState(state);
        }
        
    }

    setStartingHour = (startingHour) => {
        let state = this.state;
        
        // TODO: Validation here
        if(true){
            state.startingHour = startingHour;
            this.setState(state);
        }
    }

    setStartingMinute = (startingMinute) => {
        let state = this.state;
        
        // TODO: Validation here
        if(true){
            state.startingMinute = startingMinute;
            this.setState(state);
        }
    }

    setEndHour = (endHour) => {
        let state = this.state;
        
        // TODO: Validation here
        if(true){
            state.endHour = endHour;
            this.setState(state);
        }
    }

    setEndMinute = (endMinute) => {
        let state = this.state;
        
        // TODO: Validation here
        if(true){
            state.endMinute = endMinute;
            this.setState(state);
        }
    }

    setBreakDuration = (breakDuration) => {
        let state = this.state;
        
        // TODO: Validation here
        if(true){
            state.breakDuration = breakDuration;
            this.setState(state);
        }
    }
    
    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3}>
                    <div className="page-title">Time Entry</div>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Date</InputGroup.Text>
                        <Form.Control aria-label="Date" value={this.state.date} onChange={(e) => this.setDate(e.target.value)}/>
                    </InputGroup>
                    <div className="form-subtitle">Starting Time</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Hour</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" value={this.state.startingHour} onChange={(e) => this.setStartingHour(e.target.value)} />
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minute</InputGroup.Text>
                            <Form.Control aria-label="Starting Minute" value={this.state.startingMinute} onChange={(e) => this.setStartingMinute(e.target.value)} />
                        </InputGroup>
                    </Stack>
                    <div className="form-subtitle">End Time</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Hour</InputGroup.Text>
                            <Form.Control aria-label="End Hour" value={this.state.endHour} onChange={(e) => this.setEndHour(e.target.value)}/>
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minute</InputGroup.Text>
                            <Form.Control aria-label="End Minute" value={this.state.endMinute} onChange={(e) => this.setEndMinute(e.target.value)}/>
                        </InputGroup>
                    </Stack>
                    <div className="form-subtitle">Breaks</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minutes</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" value={this.state.breakDuration} onChange={(e) => this.setBreakDuration(e.target.value)}/>
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