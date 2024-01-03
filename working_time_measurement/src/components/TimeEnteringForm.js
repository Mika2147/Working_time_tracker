import React, { Component } from 'react';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';

class TimeEnteringForm extends Component {
    state = {  } 
    render() { 
        return (<React.Fragment>
            <div className='main-container'>
            <InputGroup className="mb-3">
                <InputGroup.Text>H</InputGroup.Text>
                <Form.Control aria-label="Starting Hour" />
            </InputGroup>
            </div>
        </React.Fragment>);
    }
}
 
export default TimeEnteringForm;