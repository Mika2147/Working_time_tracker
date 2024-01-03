import React, { Component } from 'react';
import TimeMeasurementCard from './TimeMeasurementCard';

class TimeMeasurementStart extends Component {
    state = {  } 
    render() { 
        return (<React.Fragment>
                    <div className="main-container">
                        <div className="card-container">
                            <TimeMeasurementCard title="Today" text="Enter the time you worked today" buttonTitle="Go"/>
                            <TimeMeasurementCard title="This Month" text="Look at the time your worked this month" buttonTitle="Go"/>
                            <TimeMeasurementCard title="Export" text="Export current Month" buttonTitle="Go"/>
                        </div>
                    </div>
                </React.Fragment>
        )
    }
}
 
export default TimeMeasurementStart;