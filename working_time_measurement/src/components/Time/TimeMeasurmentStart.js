import React, { Component, useEffect, useState } from 'react';
import TimeMeasurementCard from './TimeMeasurementCard';
import { useMsal } from '@azure/msal-react';
import { msalConfig, appRoles } from '../../Config';

function TimeMeasurementStart() {
  const [isAdmin, setIsAdmin] = useState(false);
  const {instance} = useMsal();

  useEffect(() => {
    var isAdmin = false;
    const currentAccount = instance.getActiveAccount();
      if (currentAccount){
        if(currentAccount.tenantId = msalConfig.auth.tenantId){
          const idTokenClaims = currentAccount.idTokenClaims;
          if (idTokenClaims && idTokenClaims.aud == msalConfig.auth.clientId && idTokenClaims["roles"]){
            if (idTokenClaims["roles"].includes(appRoles.Admin)){
              isAdmin = true;;
            }
          }
        }
      }
      setIsAdmin(isAdmin);
  });

  return ( <React.Fragment>
    <div className="main-container">
        <div className="card-container">
            <TimeMeasurementCard title="Today" text="Enter the time you worked today" buttonTitle="Go" link="/time-measurement/day"/>
            <TimeMeasurementCard title="This Month" text="Look at the time your worked this month" buttonTitle="Go" link="/time-measurement/overview"/>
            <TimeMeasurementCard title="Export" text="Export current Month" buttonTitle="Go" link="/time-measurement/export"/>
            {isAdmin && <TimeMeasurementCard title="Users" text="Manage users" buttonTitle="Go" link="/users"/>}
        </div>
    </div>
    </React.Fragment> );
}

export default TimeMeasurementStart;