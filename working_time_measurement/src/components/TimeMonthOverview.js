import React, { Component } from 'react';
import Table from 'react-bootstrap/Table';
import Pagination from 'react-bootstrap/Pagination';
import MonthPagination from './MonthPagination';
import { Stack } from 'react-bootstrap';



class TimeMonthOverview extends Component {
    state = { 
        error: null,
        isLoaded: false,
        items: [],
        month: ((new Date()).getMonth() + 1),
        year: (new Date()).getFullYear(),
     }

     componentDidMount(){
        this.fetchEntries();
     }

    fetchEntries(){
        var url = "http://localhost:8080/time";
        if (this.state.month !== undefined){
            url = url + "?month=" + this.state.month
        }
        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
            },
        };

        fetch(url, requestOptions)
        .then(res => res.json())
        .then(
        (result) => {
          this.setState({
            isLoaded: true,
            items: result.items
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error
          });
        }
      )
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
                        </tr>
                    </thead>
                    <tbody> 
                    {this.state.items.map(item => <tr>
                            <td>{item.date}</td>
                            <td>{item.startingTime}</td>
                            <td>{item.endTime}</td>
                            <td>{item.breakDuration}</td>
                            <td>{item.totalWorkingTime}</td>
                            </tr>)}
                    </tbody>
                </Table>
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
 
export default TimeMonthOverview;