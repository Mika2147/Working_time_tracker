import React, { Component } from 'react';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import { Link } from "react-router-dom";

class TimeMeasurementCard extends Component {
    state = {  } 
    render() { 
        return (<Card style={{ width: '18rem' }}>
        <Card.Body>
            <Card.Title>{this.props.title}</Card.Title>
            <Card.Text>
            {this.props.text}
            </Card.Text>
            <Link to={this.props.link}>
                <Button variant="primary" onClick={this.props.onClick}>{this.props.buttonTitle}</Button>
            </Link>
        </Card.Body>
      </Card>);
    }
}
 
export default TimeMeasurementCard;