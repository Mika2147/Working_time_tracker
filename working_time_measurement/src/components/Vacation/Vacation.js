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
            } 

    rowClicked = (navigation, id) => {
        console.log("row " + id + " clicked");
    }

    componentDidMount(){
        this.fetchEntries();
     }

    fetchEntries(){
        var url = "http://localhost:8081/vacation/future";

        var hashedUsername = md5(Cookies.get("Username"));
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

    render() { 
        return (<React.Fragment>
            <div className='main-container'>
            <Stack direction="vertical" gap={3}>
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
                <YearPagination year={2024}/>
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