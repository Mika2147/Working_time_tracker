import React, { Component } from 'react';
import NavigationBar from './NavigationBar';
import TimeMeasurementStart from './TimeMeasurmentStart';
import TimeEnteringForm from './TimeEnteringForm';
import { BrowserRouter, Route, Routes } from 'react-router-dom';

class App extends Component {
    state = {  } 
    render() { 
        return <React.Fragment>
            <NavigationBar/>
            <BrowserRouter>
                <Routes>
                    <Route path="/" element={<TimeMeasurementStart />} />
                    <Route path="/new-entry" element={<TimeEnteringForm />}/>
                </Routes>
            </BrowserRouter>
        </React.Fragment>
    }
}
 
export default App;