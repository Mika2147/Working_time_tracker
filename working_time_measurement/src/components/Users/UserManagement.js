import Cookies from 'js-cookie';
import { md5 } from 'js-md5';
import React, { Component } from 'react';
import { Button, Stack, Table } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';
import { Token } from '../../Token';


class UserManagement extends Component {
    state = { 
                items: [], 
            }

    fetchEntries = async () =>{
        var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8080") +"/user";
        
        var token = await Token.getToken();

        const requestOptions = {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': token,
            },
        };

        fetch(url, requestOptions)
        .then(res => res.json())
        .then(
        (result) => {
          this.setState({
            items: result
          });
        },
        (error) => {
          console.log(error);
        }
      )
    }

    componentDidMount(){
        this.fetchEntries();
    }

    /*deleteUser = async (id) => {
        var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8083") + "/admin/users/" + id;
        
        var hashedUsername = Cookies.get("Username");
        var token = await Token.getToken();


        const requestOptions = {
            method: 'DELETE',
            headers: { 
                'Content-Type': 'application/json',
                'Authorization': ("Basic " + hashedUsername + ":" + token)
            },
        };

        fetch(url, requestOptions)
        .then(res => res.json())
        .then(
        (result) => {
            console.log("User " + id + " deleted")
        },
        (error) => {
          console.log(error);
        }
      )

      this.fetchEntries();
    }*/

    /*changeUser = (id, name, isAdmin, navigation) => {
        
        var url = "/users/edit?id=" + id + "&name=" + name + "&isAdmin=" + isAdmin;

        navigation(url);
    }*/

    showTimeEntries = (name, navigation) => {
        
        var url = "/time-measurement/overview?username=" + name;

        navigation(url);
    }

    showVacationEntries = (name, navigation) => {
        
        var url = "/vacation?username=" + name;

        navigation(url);
    }

    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3} className='central_alignment'>
                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>Username</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody> 
                        {this.state.items.map(item => <UserRow username={item} showTimeEntries={this.showTimeEntries} showVacationEntries={this.showVacationEntries}/>)}
                        </tbody>
                    </Table>
                </Stack>
            </div>
        </React.Fragment>);
    }
}

function UserRow(props){
    const navigation = useNavigate();
    return(
        <tr>
            <td>{props.username}</td>
            <td>
            <Button variant="primary" onClick={(() => props.showTimeEntries(props.username, navigation))}>Working Times</Button>
            <Button variant="primary" onClick={(() => props.showVacationEntries(props.username, navigation))}>Vacation</Button>
            </td>
            <td></td>
        </tr>
    )
}
 
export default UserManagement;