import React, {Component} from 'react';

class UserList extends Component {

  state = {
    users: []
  };

  async componentDidMount(){
    const response = await fetch('/users');
    console.log(response);
  }

  render(){
    return <div>
      <p>yeah</p>
    </div>
  }
}

export default UserList;
