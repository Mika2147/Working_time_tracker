import React, { Component } from 'react';
import { Button, Stack, CloseButton } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import { useNavigate, useSearchParams} from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';

class TimeEnteringForm extends Component {
    state = {
        date: this.props.date != undefined ? this.props.date :((new Date()).toLocaleDateString("de-de")),
        startingHour: (this.props.startingHour != undefined ? this.props.startingHour :((new Date()).getHours()).toString()),
        startingMinute: (this.props.startingMinute != undefined ? this.props.startingMinute :((new Date()).getMinutes()).toString()),
        endHour: (this.props.endHour != undefined ? this.props.endHour :((new Date()).getHours()).toString()),
        endMinute: (this.props.endMinute != undefined ? this.props.endMinute :((new Date()).getMinutes()).toString()),
        breakDuration: (this.props.breakDuration != undefined ? this.props.breakDuration : "0"),
        error: {
            date: false,
            startingHour: false,
            startingMinute: false,
            endHour: false,
            endMinute: false,
            breakDuration: false,
        }
    }

    fetchEntry = () => {
        var envUrl = process.env.REACT_APP_TIME_URL;
        var url = (envUrl != undefined ? envUrl : "http://localhost:8080") + "/time/day";
        let state = this.state;
        const date = this.createDateFromDateString(state.date);

        var hashedUsername = md5(Cookies.get("Username"));
        var token = Cookies.get("Token");

        if (date !== undefined){
            url = url + "?day=" + date.getDate();
            url = url + "&month=" + (date.getMonth() + 1);
            url = url + "&year=" + date.getFullYear();
        }
        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token),
            },
        };

        fetch(url, requestOptions)
        .then(res => res.json())
        .then(
        (result) => {
          let state = this.state;
          state.startingHour = result.startingHour;
          state.startingMinute = result.startingMinute;
          state.endHour = result.endHour;
          state.endMinute = result.endMinute;
          state.breakDuration = result.breakDuration;
          state = this.searchFormErrors(state);
          this.setState(state);

        },
        (error) => {
         console.log("No existing entry found for date: " + this.state.date);
        }
      )
    }

    createDateFromDateString = (dateString) => {
        const [day, month, year] = dateString.split('.');
      
        const dateObject = new Date(parseInt(year), parseInt(month) - 1, parseInt(day));
      
        return dateObject;
    }

    componentDidMount(){
        this.fetchEntry();
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

        var envUrl = process.env.REACT_APP_TIME_URL;
        var url = (envUrl != undefined ? envUrl : "http://localhost:8080") + "/time";

        var hashedUsername = md5(Cookies.get("Username"));
        var token = Cookies.get("Token");

        const requestOptions = {
            method: 'POST',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token)
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
            if (res.status > 199 && res.status < 300){
                navigation("/time-measurement");
            }
        },
        (error) => {
          console.log(error)
        });
    }

    closeForm = (navigation) => {
        navigation("/time-measurement");
    }

    setDate = (date) => {
        let state = this.state;        

        state.date = date;
        state = this.searchFormErrors(state)
        this.setState(state);

        this.fetchEntry();
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

        if (!this.isValidDate(date)){
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
        let minuteRegex = /^(\d{1,2})$/;
        
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

        let breakRegex = /^(\d{1,3})$/;
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
                        <div className="page-title">Time Entry</div>
                        <CloseFormButton closeFunction={this.closeForm}/>
                    </Stack>
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

const withQueryDateHOC = (Component) =>{
    return (props) =>{
        const [searchParams, setSearchParams] = useSearchParams();
    let day = searchParams.get("day");
    let month = searchParams.get("month");
    let year = searchParams.get("year");
    let date = undefined
    if(day !== undefined && month !== undefined && year != undefined){
        date = (day + "." + month + "." + year);
    }

    return <Component date={date} />

    }
    

    return undefined;
}
 
export default withQueryDateHOC(TimeEnteringForm);