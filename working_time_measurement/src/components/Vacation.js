import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import { useNavigate, Link } from 'react-router-dom';
import Table from 'react-bootstrap/Table';

class Vacation extends Component {
    state = { items: [
                        {
                            startingDate: "1.1.2024",
                            endDate: "31.1.2024",
                            vacationDays: 22,
                            accepted: true,
                        },
                    ], 
                    restVacationDays: 8,
                } 

    rowClicked = (navigation, id) => {
        console.log("row " + id + " clicked");
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