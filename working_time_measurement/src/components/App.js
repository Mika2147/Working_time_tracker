import React, { Component } from 'react';
import NavigationBar from './NavigationBar';
import TimeMeasurementStart from './Time/TimeMeasurmentStart';
import TimeEnteringForm from './Time/TimeEnteringForm';
import TimeMonthOverview from './Time/TimeMonthOverview';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Vacation from './Vacation';
import TimeExport from './Time/TimeExport';
import Login from './Login';
import CheckLoginWrapper from './CheckLoginWrapper';

class App extends Component {
    state = {  } 
    render() { 
        return <React.Fragment>
            <BrowserRouter>
            <NavigationBar/>

                <Routes>
                    <Route path="/login" element={<Login />}/>
                    <Route path="/" element={<CheckLoginWrapper><TimeMeasurementStart /></CheckLoginWrapper>}/>
                    <Route path="/time-measurement" element={<CheckLoginWrapper><TimeMeasurementStart /></CheckLoginWrapper>}/>
                    <Route path="/vacation" element={<CheckLoginWrapper><Vacation /></CheckLoginWrapper>}/>

                    <Route path="/time-measurement/day" element={<CheckLoginWrapper><TimeEnteringForm /></CheckLoginWrapper>}/>
                    <Route path="/time-measurement/overview" element={<CheckLoginWrapper><TimeMonthOverview /></CheckLoginWrapper>}/>
                    <Route path="/time-measurement/export" element={<CheckLoginWrapper><TimeExport /></CheckLoginWrapper>}/>
                </Routes>
            </BrowserRouter>
        </React.Fragment>
    }
}
 
export default App;