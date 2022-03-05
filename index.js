/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';

console.log("in index.js");
AppRegistry.registerComponent(appName, () => App);
