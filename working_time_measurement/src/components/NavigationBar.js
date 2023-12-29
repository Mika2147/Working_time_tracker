import React, { Component } from 'react';
import { Navbar, Container, Nav, NavDropdown } from 'react-bootstrap';

class NavigationBar extends Component {
    state = {  } 
    render() { 
        return <Navbar bg="dark" expand="lg" variant="dark" className="border-bottom border-body">
        <Container>
          <Navbar.Brand href="/">Time Inc</Navbar.Brand>
          <Navbar.Toggle aria-controls="navbarSupportedContent" />
          <Navbar.Collapse id="navbarSupportedContent">
            <Nav className="me-auto">
              <Nav.Link href="/">Time Measurement</Nav.Link>
              <Nav.Link href="/">Vacation</Nav.Link>
            </Nav>
            <Nav>
              <button className="btn btn-danger" href="/">Logout</button>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>;
    }
}
 
export default NavigationBar;