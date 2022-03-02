/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React from 'react';
import {View, Text} from 'react-native';
import Alt from './kotlin';

export default class App extends React.Component {
  state = {res: 0};

  async componentDidMount() {
    let tmp = await Alt.trigger(10, 3);
    console.log(tmp);
    this.setState({res: tmp});
  }

  render() {
    return (
      <View>
        <Text>Hello ReactNative</Text>
        <Text>this is a+b from kotlin: {this.state.res}</Text>
      </View>
    );
  }
}
