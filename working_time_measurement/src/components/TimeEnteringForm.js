import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';

class TimeEnteringForm extends Component {
    state = {  } 
    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3}>
                    <div className="page-title">Time Entry</div>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Date</InputGroup.Text>
                        <Form.Control aria-label="Date" />
                    </InputGroup>
                    <div className="form-subtitle">Starting Time</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Hour</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" />
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minute</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" />
                        </InputGroup>
                    </Stack>
                    <div className="form-subtitle">End Time</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Hour</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" />
                        </InputGroup>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minute</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" />
                        </InputGroup>
                    </Stack>
                    <div className="form-subtitle">Breaks</div>
                    <Stack direction="horizontal" gap={3}>
                        <InputGroup className="mb-3">
                            <InputGroup.Text>Minutes</InputGroup.Text>
                            <Form.Control aria-label="Starting Hour" />
                        </InputGroup>
                    </Stack>
                    <Button>Submit</Button>
                </Stack>
                
            </div>
        </React.Fragment>);
    }
}
 
export default TimeEnteringForm;