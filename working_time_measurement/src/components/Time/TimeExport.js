import React, { Component } from 'react';
import { Button, Stack } from 'react-bootstrap';
import Form from 'react-bootstrap/Form';
import InputGroup from 'react-bootstrap/InputGroup';
import Cookies from 'js-cookie';
import { Token } from '../../Token';


class TimeExport extends Component {
    state = { 
        month: 1,
        year: 1971,
        token: "",
     }

     months = {
        1: "January",
        2: "February",
        3: "March",
        4: "April",
        5: "May",
        6: "June",
        7: "July",
        8: "August",
        9: "September",
        10: "October",
        11: "November",
        12: "December",
     }

     years = [0]

     prepareDwonload = async () => {
        var envUrl = process.env.REACT_APP_TIME_URL;
        var url = (envUrl != undefined ? envUrl : "http://localhost:8080") + "/time/export/prepare";
        let state = this.state;

        var token = await Token.getToken();

        
        url = url + "?month=" + this.state.month;
        url = url + "&year=" + this.state.year;
        
        const requestOptions = {
            method: 'GET',
            headers: { 
                'Authorization': (token),
            },
        };

        fetch(url, requestOptions)
        .then(res => res.text())
        .then(
        (result) => {
          let state = this.state;
          state.token = result;
          this.setState(state);

        },
        (error) => {
         console.log("Export preparation failed");
        }
      )
    }
     

     setMonth = (month) => {
        let state = this.state;
        if(month > 0 && month < 13){
            state.month = month;
            state.link = this.createLink(state.month, state.year);
            this.setState(state);
        }
     }

     setYear = (year) => {
        let state = this.state;
        let currentYear = (new Date()).getFullYear();
        if(year > 0 && year < (currentYear + 1)){
            state.year = year;
            state.link = this.createLink(state.month, state.year);
            this.setState(state);
        }
     }

    componentDidMount(){
        let state = this.state;
        let date = new Date();
        state.year = date.getFullYear();
        state.month = (date.getMonth() + 1);
        this.years = arrayRange(1970, (new Date()).getFullYear(), 1);
        state.link = this.createLink(state.month, state.year)

        this.setState(state);
    }

    createLink = (month, year) => {
        var envUrl = process.env.REACT_APP_TIME_URL;
        var url = (envUrl != undefined ? envUrl : "http://localhost:8080") + "/time/export/" + this.state.token;
        
        return url;
    }

    render() { 
        return (<React.Fragment>
            <div className='main-container'>
                <Stack direction="vertical" gap={3}>
                    <div className="page-title">Export</div>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Month</InputGroup.Text>
                        <Form.Select aria-label="Default select example" value={this.state.month} onChange={e => this.setMonth(e.target.value)}>
                            {Object.keys(this.months).map((key) => <option value={key}>{this.months[key]}</option>)}
                        </Form.Select>
                    </InputGroup>
                    <InputGroup className="mb-3">
                        <InputGroup.Text>Year</InputGroup.Text>
                        <Form.Select aria-label="Default select example" value={this.state.year} onChange={e => this.setYear(e.target.value)}>
                            {this.years.map((year) => <option value={year}>{year}</option>)}
                        </Form.Select>
                    </InputGroup>
                    <Button onClick={() => this.prepareDwonload()}>Prepare Download</Button>

                    {this.state.token !=="" ? <a href={this.createLink()} download={this.state.month + "_" + this.state.year + ".xlsx"} className='full-width'>
                        <Button className='full-width'>Download</Button>
                    </a> : <div></div>}
                </Stack>
            </div>
        </React.Fragment>);
    }
}

function arrayRange(start, stop, step){
    return Array.from({ length: (stop - start) / step + 1 }, (value, index) => start + index * step);
}
 
export default TimeExport;