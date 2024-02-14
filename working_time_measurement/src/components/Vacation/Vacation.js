import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import Table from 'react-bootstrap/Table';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';
import YearPagination from './YearPagination';

class Vacation extends Component {
    state = { items: [], 
                restVacationDays: 0,
                year: (new Date()).getFullYear(),
            } 

    rowClicked = (navigation, id) => {
        console.log("row " + id + " clicked");
    }

    componentDidMount(){
        this.fetchEntries();
     }

    fetchEntries(){
        var envUrl = process.env.REACT_APP_VACATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8081") + "/vacation?year=" + this.state.year;

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
          this.setState({
            items: result.items,
            restVacationDays: result.restVacationDays,
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

    selectionChanged = (year) => {
        let state = this.state;
        state.year = year;
        this.setState(state);
        this.fetchEntries();
    }

    render() { 
        return (<React.Fragment>
            <div className='main-container'>
            <Stack direction="vertical" gap={3} className='central_alignment'>
                <Link to="/vacation/new">
                    <Button>New Entry</Button>
                </Link>
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Starting Date</th>
                            <th>End Date</th>
                            <th>Vacation Days</th>
                            <th>Accepted</th>
                        </tr>
                    </thead>
                    <tbody> 
                    {this.state.items.map(item => <VacationRow clicked={this.rowClicked} startingDate={item.startingDate} endDate={item.endDate} vacationDays={item.vacationDays} accepted={item.accepted}/>)}
                    </tbody>
                </Table>
                <div>
                    Rest Vacation Days: {this.state.restVacationDays}
                </div>
                <YearPagination year={this.state.year} callback={(year) => this.selectionChanged(year)}/>
            </Stack>
            </div>
        </React.Fragment>);
    }
}

function VacationRow(props){
    const navigation = useNavigate();
    return(
        <tr onClick={() => props.clicked(navigation, props.id)}>
            <td>{props.startingDate}</td>
            <td>{props.endDate}</td>
            <td>{props.vacationDays}</td>
            <td>{props.accepted ? "Yes" : "No"}</td>
            </tr>
    )
}
 
export default Vacation;