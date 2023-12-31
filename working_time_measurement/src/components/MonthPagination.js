import React, { Component } from 'react';
import { Pagination } from 'react-bootstrap';

class MonthPagination extends Component {
    state = { 
                active: this.props.month,
                year: this.props.year,
            }
    
    clickedItem = (index) => {
        let localState = this.state;
        localState.active = index;
        this.setState(localState);
    }
    
    render() { 
        return (<React.Fragment>
            <Pagination>
                <Pagination.Item key={1} active={1 === this.state.active} onClick={() => {this.clickedItem(1); this.props.callback(1);}}>
                    January
                </Pagination.Item>
                <Pagination.Item key={2} active={2 === this.state.active} onClick={() => {this.clickedItem(2); this.props.callback(2);}}>
                    February
                </Pagination.Item>
                <Pagination.Item key={3} active={3 === this.state.active} onClick={() => {this.clickedItem(3); this.props.callback(3);}}>
                    March
                </Pagination.Item>
                <Pagination.Item key={4} active={4 === this.state.active} onClick={() => {this.clickedItem(4); this.props.callback(4);}}>
                    April
                </Pagination.Item>
                <Pagination.Item key={5} active={5 === this.state.active} onClick={() => {this.clickedItem(5); this.props.callback(5);}}>
                    May
                </Pagination.Item>
                <Pagination.Item key={6} active={6 === this.state.active} onClick={ () => {this.clickedItem(6); this.props.callback(6);}}>
                    June
                </Pagination.Item>
                <Pagination.Item key={7} active={7 === this.state.active} onClick={() => {this.clickedItem(7); this.props.callback(7);}}>
                    July
                </Pagination.Item>
                <Pagination.Item key={8} active={8 === this.state.active} onClick={() => {this.clickedItem(8); this.props.callback(8);}}>
                    August
                </Pagination.Item>
                <Pagination.Item key={9} active={9 === this.state.active} onClick={() => {this.clickedItem(9); this.props.callback(9);}}>
                    September
                </Pagination.Item>
                <Pagination.Item key={10} active={10 === this.state.active} onClick={() => {this.clickedItem(10); this.props.callback(10);}}>
                    October
                </Pagination.Item>
                <Pagination.Item key={11} active={11 === this.state.active} onClick={() => {this.clickedItem(11); this.props.callback(11);}}>
                    November
                </Pagination.Item>
                <Pagination.Item key={12} active={12 === this.state.active} onClick={() => {this.clickedItem(12); this.props.callback(12);}}>
                    December
                </Pagination.Item>
            </Pagination>
            <div className='year_label'>{this.state.year}</div>
        </React.Fragment>);
    }
}
 
export default MonthPagination;