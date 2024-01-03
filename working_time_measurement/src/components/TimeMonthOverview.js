import React, { Component } from 'react';
import Table from 'react-bootstrap/Table';
import Pagination from 'react-bootstrap/Pagination';
import MonthPagination from './MonthPagination';
import { Stack } from 'react-bootstrap';



class TimeMonthOverview extends Component {
    state = { 
        items: [
            {
                date: "02.01.2024", 
                starting_time: "08:00",
                end_time: "17:00",
                break_duration: "60",
                total_working_time: "8:00"
            },
            {
                date: "03.01.2024", 
                starting_time: "08:05",
                end_time: "17:30",
                break_duration: "50",
                total_working_time: "8:35"
            }
        ]
     }

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
                            <td>{item.starting_time}</td>
                            <td>{item.end_time}</td>
                            <td>{item.break_duration}</td>
                            <td>{item.total_working_time}</td>
                            </tr>)}
                    </tbody>
                </Table>
                <MonthPagination/>
                </Stack>
            </div>
        </React.Fragment>);
    }
}
 
export default TimeMonthOverview;