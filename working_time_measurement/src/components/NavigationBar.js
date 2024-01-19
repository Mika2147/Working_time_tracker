import Cookies from 'js-cookie';
import React, { Component } from 'react';
import { Navbar, Container, Nav, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

class NavigationBar extends Component {
    state = {  } 
    render() { 
        return <Navbar bg="dark" expand="lg" variant="dark" className="border-bottom border-body">
        <Container>
          <Navbar.Brand href="/">Time Inc</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarSupportedContent" />
          <Navbar.Collapse id="navbarSupportedContent">
            <Nav className="me-auto">
              <Link to="/time-measurement" className="link">
                <Nav.Link href="/">Time Measurement</Nav.Link>
              </Link>
              <Link to="/vacation" className="link">
                <Nav.Link href="/">Vacation</Nav.Link>
              </Link>
            </Nav>
            <Nav>
              <Button variant="danger" href="/" onClick={() => logout()}>Logout</Button>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>;
    }
}

function logout(){
  var url = "http://localhost:8083/token-validation";
        const token = Cookies.get("Token");


        const requestOptions = {
            method: 'GET',
            headers: { 
                'Authorization': ('Basic ' + token),
            },
        };

        fetch(url, requestOptions)
        .then(res => res.status)
        .then(
        (result) => {
          if (result === 200){
            Cookies.remove("Token");
          }

        },
        (error) => {
            console.log("Failed to log out");  
        }
      )
}
 
export default NavigationBar;