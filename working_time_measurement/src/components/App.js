import React, { Component } from 'react';
import NavigationBar from './NavigationBar';
import TimeMeasurementStart from './Time/TimeMeasurmentStart';
import TimeEnteringForm from './Time/TimeEnteringForm';
import TimeMonthOverview from './Time/TimeMonthOverview';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Vacation from './Vacation/Vacation';
import TimeExport from './Time/TimeExport';
import Login from './Login';
import CheckLoginWrapper from './CheckLoginWrapper';
import VacationForm from './Vacation/VacationForm';
import UserManagement from './Users/UserManagement';
import UserRegistrationForm from './Users/UserRegistartionForm';
import UserEditForm from './Users/UserEditFrom';
import { appRoles, config } from "../Config";
import { PublicClientApplication, IPublicClientApplication } from "@azure/msal-browser";
import { MsalProvider } from "@azure/msal-react";

class App extends Component {
    //state = {  } 
    render() {
        return <React.Fragment>
            <MsalProvider instance={this.props.pca}>
                <BrowserRouter>
                    <NavigationBar />
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><TimeMeasurementStart /></CheckLoginWrapper>} />
                        <Route path="/time-measurement" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><TimeMeasurementStart /></CheckLoginWrapper>} />
                        <Route path="/vacation" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><Vacation/></CheckLoginWrapper>} />
                        <Route path="/vacation/new" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><VacationForm /></CheckLoginWrapper>} />

                        <Route path="/users" element={<CheckLoginWrapper roles={[appRoles.Admin]}><UserManagement /></CheckLoginWrapper>} />
                        <Route path="/users/new" element={<CheckLoginWrapper roles={[appRoles.Admin]}><UserRegistrationForm /></CheckLoginWrapper>} />
                        <Route path="/users/edit" element={<CheckLoginWrapper roles={[appRoles.Admin]}><UserEditForm /></CheckLoginWrapper>} />

                        <Route path="/time-measurement/day" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><TimeEnteringForm /></CheckLoginWrapper>} />
                        <Route path="/time-measurement/overview" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><TimeMonthOverview /></CheckLoginWrapper>} />
                        <Route path="/time-measurement/export" element={<CheckLoginWrapper roles={[appRoles.User, appRoles.Admin]}><TimeExport /></CheckLoginWrapper>} />
                    
                    </Routes>
                </BrowserRouter>
            </MsalProvider> 
        </React.Fragment>
    }
}

export default App;