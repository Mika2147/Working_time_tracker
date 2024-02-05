import React, { Component } from 'react';
import TimeMeasurementCard from './TimeMeasurementCard';
import Cookies from 'js-cookie';
import { md5 } from 'js-md5';

class TimeMeasurementStart extends Component {
    state = { isAdmin: false } 

    fetchIsAdmin(){
        var url = "http://localhost:8083/register";
        
        var hashedUsername = md5(Cookies.get("Username"));
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
            isAdmin: result,
          });
        },
        (error) => {
          console.log(error);
        }
      )
    }

    componentDidMount(){
        this.fetchIsAdmin();
    }

    render() { 
        return (<React.Fragment>
                    <div className="main-container">
                        <div className="card-container">
                            <TimeMeasurementCard title="Today" text="Enter the time you worked today" buttonTitle="Go" link="/time-measurement/day"/>
                            <TimeMeasurementCard title="This Month" text="Look at the time your worked this month" buttonTitle="Go" link="/time-measurement/overview"/>
                            <TimeMeasurementCard title="Export" text="Export current Month" buttonTitle="Go" link="/time-measurement/export"/>
                            {this.state.isAdmin && <TimeMeasurementCard title="Users" text="Manage users" buttonTitle="Go" link="/users"/>}
                        </div>
                    </div>
                </React.Fragment>
        )
    }
}
 
export default TimeMeasurementStart;