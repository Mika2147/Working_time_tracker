import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import Table from 'react-bootstrap/Table';
import Cookies from 'js-cookie';
import YearPagination from './YearPagination';

class Vacation extends Component {
    state = { items: [], 
                restVacationDays: 0,
                year: (new Date()).getFullYear(),
                username: this.props.username,
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

        if(this.state.username !== ""){
            url = url + "&username=" + this.state.username;
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

    acceptVacation = (username, id) => {
        var envUrl = process.env.REACT_APP_VACATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8081") + "/vacation/accept?id=" + id + "&username=" + username;

        var hashedUsername = Cookies.get("Username");
        var token = Cookies.get("Token");

        const requestOptions = {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token)
            },
        };

        fetch(url, requestOptions)
        .then(res => res.json())
        .then(
        (result) => {
            console.log(result);
          this.fetchEntries();
        },
        (error) => {
          console.log(error);
        }
      )
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
                    {this.state.items.map(item => <VacationRow clicked={this.rowClicked} id={item.id} startingDate={item.startingDate} endDate={item.endDate} vacationDays={item.vacationDays} accepted={item.accepted} username={this.state.username} acceptVacation={this.acceptVacation}/>)}
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
            <td>{props.accepted ? "Yes" : "No"}{(props.username !== "" && !props.accepted) ? <Button variant="primary" onClick={(() => props.acceptVacation(props.username, props.id))}>Accept</Button> : <div></div>}</td>
            </tr>
    )
}

const withQueryParamsHOC = (Component) =>{
    return (props) =>{
        const [searchParams, setSearchParams] = useSearchParams();
    var username = searchParams.get("username");
    username = username !== undefined ? username : "";
    username = username !== null ? username : "";

    return <Component username={username}/>

    }
}
 
export default withQueryParamsHOC(Vacation);