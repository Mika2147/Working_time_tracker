import React, { Component } from 'react';
import Table from 'react-bootstrap/Table';
import MonthPagination from './MonthPagination';
import { Stack } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';



class TimeMonthOverview extends Component {
    state = { 
        error: null,
        isLoaded: false,
        items: [],
        month: ((new Date()).getMonth() + 1),
        year: (new Date()).getFullYear(),
        timeBalance: 0.0,
     }

     componentDidMount(){
        this.fetchEntries();
     }

    fetchEntries(){
        var envUrl = process.env.REACT_APP_TIME_URL;
        var url = (envUrl != undefined ? envUrl : "http://localhost:8080") + "/time";
        if (this.state.month !== undefined){
            url = url + "?month=" + this.state.month
        }

        var hashedUsername = Cookies.get("Username");
        var token = Cookies.get("Token");

        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token)
            },
        };

        fetch(url, requestOptions)
        .then(res => res.json())
        .then(
        (result) => {
            let state = this.state;
            state.items = result.items;
            state.isLoaded = true;
            state.timeBalance = result.timeBalance;
            this.setState(state);
            debugger;
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error
          });
        }
      )
    }

    rowClicked = (navigation, dateString) => {
        let date = this.createDateFromDateString(dateString);
        var url = "/time-measurement/day";
        if (date !== undefined){
            url = url + "?day=" + date.getDate();
            url = url + "&month=" + (date.getMonth() + 1);
            url = url + "&year=" + date.getFullYear();
        }

        navigation(url);
    }

    createDateFromDateString = (dateString) => {
        const [day, month, year] = dateString.split('.');
      
        const dateObject = new Date(parseInt(year), parseInt(month) - 1, parseInt(day));
      
        return dateObject;
    }

     /* TODO: 
        - Table Rows should be links to a view that edits the date
     */
    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3} className='central_alignment'>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Starting Time</th>
                            <th>End Time</th>
                            <th>Break Duration</th>
                            <th>Total Working Time</th>
                            <th>Tasks</th>
                            <th>Comment</th>
                        </tr>
                    </thead>
                    <tbody> 
                    {this.state.items.map(item => <DayRow clicked={this.rowClicked} date={item.date} startingTime={item.startingTime} endTime={item.endTime} breakDuration={item.breakDuration} totalWorkingTime={item.totalWorkingTime} tasks={item.tasks} comment={item.comment}/>)}
                    </tbody>
                </Table>
                Time Balance: {this.state.timeBalance}
                <MonthPagination month={this.state.month} year={this.state.year} callback={(month) => {
                    let state = this.state;
                    state.month = month;
                    this.setState(state);
                    this.fetchEntries();
                }}/>
                </Stack>
            </div>
        </React.Fragment>);
    }
}

function DayRow(props){
    const navigation = useNavigate();
    return(
        <tr onClick={() => props.clicked(navigation, props.date)}>
            <td>{props.date}</td>
            <td>{props.startingTime}</td>
            <td>{props.endTime}</td>
            <td>{props.breakDuration}</td>
            <td>{Math.floor(props.totalWorkingTime / 60)}:{(props.totalWorkingTime % 60).toLocaleString("de-de", {minimumIntegerDigits: 2})}</td>
            <td>{props.tasks}</td>
            <td>{props.comment}</td>
            </tr>
    )
}
 
export default TimeMonthOverview;