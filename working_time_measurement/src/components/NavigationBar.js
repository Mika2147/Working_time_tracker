import React, { Component } from 'react';
import { Navbar, Container, Nav, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';
import { Token } from '../Token';


class NavigationBar extends Component {
  state = {}
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

function logout() {
  var envUrl = process.env.REACT_APP_AUTHORIZATION_URL;
  var url = (envUrl != undefined ? envUrl : "http://localhost:8083") + "/logout";
  const token = Token.getToken();

  const requestOptions = {
    method: 'GET',
    headers: {
      'Authorization': (token),
    },
  };

  fetch(url, requestOptions)
    .then(res => res.status)
    .then(
      (result) => {
        if (result === 200) {
          Cookies.remove("Token");
        }

      },
      (error) => {
        console.log("Failed to log out");
      }
    )
}

export default NavigationBar;