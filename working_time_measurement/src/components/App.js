import React, { Component } from 'react';
import NavigationBar from './NavigationBar';
import TimeMeasurementStart from './TimeMeasurmentStart';

class App extends Component {
    state = {  } 
    render() { 
        return <React.Fragment>
            <NavigationBar/>
            <TimeMeasurementStart/>
        </React.Fragment>
    }
}
 
export default App;