import React, { Component } from 'react';
import { Pagination } from 'react-bootstrap';

class YearPagination extends Component {
    state = { 
                current: this.props.year,
                active: this.props.year,
            }
    
    clickedItem = (index) => {
        let localState = this.state;
        localState.active = index;
        this.setState(localState);
    }
    
    render() { 
        return (<React.Fragment>
            <Pagination>
            <Pagination.Item key={this.state.current - 1} active={(this.state.current -1) === this.state.active} onClick={() => {this.clickedItem(this.state.current -1); this.props.callback(this.state.current - 1);}}>
                    {this.state.current - 1}
                </Pagination.Item>
                <Pagination.Item key={this.state.current} active={this.state.current === this.state.active} onClick={() => {this.clickedItem(this.state.current); this.props.callback(this.state.current);}}>
                    {this.state.current}
                </Pagination.Item>
                <Pagination.Item key={this.state.current + 1} active={(this.state.current + 1) === this.state.active} onClick={() => {this.clickedItem(this.state.current + 1); this.props.callback(this.state.current + 1);}}>
                    {this.state.current + 1}
                </Pagination.Item>
            </Pagination>
        </React.Fragment>);
    }
}
 
export default YearPagination;