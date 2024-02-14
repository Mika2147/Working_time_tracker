import Cookies from 'js-cookie';
import { md5 } from 'js-md5';
import React, { Component } from 'react';
import { Button, Stack, Table } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';

class UserManagement extends Component {
    state = { 
                items: [], 
            }

    fetchEntries(){
        var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8083") +"/admin/users";
        
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
            items: result.items
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

    deleteUser = (id) => {
        var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;

        var url = (envUrl != undefined ? envUrl : "http://localhost:8083") + "/admin/users/" + id;
        
        var hashedUsername = Cookies.get("Username");
        var token = Cookies.get("Token");

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
    }

    changeUser = (id, name, isAdmin, navigation) => {
        
        var url = "/users/edit?id=" + id + "&name=" + name + "&isAdmin=" + isAdmin;

        navigation(url);
    }

    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3} className='central_alignment'>
                    <Link to="/users/new">
                        <Button>New User</Button>
                    </Link>

                    <Table striped bordered hover>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>is Admin</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody> 
                        {this.state.items.map(item => <UserRow id={item.id} username={item.name} isAdmin={item.admin} deleteUser={this.deleteUser} changeUser={this.changeUser}/>)}
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
            <td>{props.id}</td>
            <td>{props.username}</td>
            <td>{props.isAdmin ? "Yes" : "No"}</td>
            <td><Button variant="primary" onClick={(() => props.changeUser(props.id, props.username, props.isAdmin, navigation))}>Edit</Button><Button variant="danger" onClick={() => props.deleteUser(props.id)}>Delete</Button></td>
            <td></td>
        </tr>
    )
}
 
export default UserManagement;