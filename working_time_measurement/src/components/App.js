import React, { Component } from 'react';
import NavigationBar from './NavigationBar';
import TimeMeasurementStart from './TimeMeasurmentStart';
import TimeEnteringForm from './TimeEnteringForm';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Vacation from './Vacation';

class App extends Component {
    state = {  } 
    render() { 
        return <React.Fragment>
            <BrowserRouter>
            <NavigationBar/>

                <Routes>
                    <Route path="/" element={<TimeMeasurementStart />} />
                    <Route path="/new-entry" element={<TimeEnteringForm />}/>
                    <Route path="/time-measurement" element={<TimeMeasurementStart />}/>
                    <Route path="/vacation" element={<Vacation />}/>
                </Routes>
            </BrowserRouter>
        </React.Fragment>
    }
}
 
export default App;