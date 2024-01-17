import React, { Component } from 'react';
import NavigationBar from './NavigationBar';
import TimeMeasurementStart from './Time/TimeMeasurmentStart';
import TimeEnteringForm from './Time/TimeEnteringForm';
import TimeMonthOverview from './Time/TimeMonthOverview';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Vacation from './Vacation';
import TimeExport from './Time/TimeExport';
import Login from './Login';

class App extends Component {
    state = {  } 
    render() { 
        return <React.Fragment>
            <BrowserRouter>
            <NavigationBar/>

                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/time-measurement" element={<TimeMeasurementStart />}/>
                    <Route path="/vacation" element={<Vacation />}/>

                    <Route path="/time-measurement/day" element={<TimeEnteringForm />}/>
                    <Route path="/time-measurement/overview" element={<TimeMonthOverview />}/>
                    <Route path="/time-measurement/export" element={<TimeExport />}/>
                </Routes>
            </BrowserRouter>
        </React.Fragment>
    }
}
 
export default App;