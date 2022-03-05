import 'react-native-reanimated';
import React, {useEffect, useState} from 'react';
import {View, Text, StyleSheet} from 'react-native';
// import InterfaceMovenet from './kotlin';
import {
  useCameraDevices,
  Camera,
  useFrameProcessor,
} from 'react-native-vision-camera';

console.log('in app.js');

export function MoveNet(frame) {
  'worklet';
  return __MoveNet(frame);
}

export default function App() {
  const devices = useCameraDevices('wide-angle-camera');
  const frontDevice = devices.front; // front camera
  const backDevice = devices.back; // back camera

  const [cameraPermission, setCameraPermission] = useState();
  const [microphonePermission, setMicrophonePermission] = useState();

  const frameProcessor = useFrameProcessor(frame => {
    'worklet';
    console.log('[-----------FRAME-----------]');
    let result = MoveNet(frame);
    console.log('[frame result]: ', result);
  }, []);

  useEffect(() => {
    async function fetchData() {
      const newCameraPermission = await Camera.requestCameraPermission();
      const newMicrophonePermission =
        await Camera.requestMicrophonePermission();
      Camera.getCameraPermissionStatus().then(setCameraPermission);
      Camera.getMicrophonePermissionStatus().then(setMicrophonePermission);
    }
    fetchData();
  }, []);

  if (cameraPermission == null || microphonePermission == null) {
    // still loading
    return <Text>No permission yet... </Text>; // loading page...
  }

  if (frontDevice == null || backDevice == null) {
    return <Text>No Device Here</Text>; // no device page...
  }

  return (
    <>
      <Camera
        style={styles.cameraStyle}
        device={frontDevice}
        isActive={true}
        frameProcessor={frameProcessor}
        frameProcessorFps={1}
      />
      <Text style={styles.box}>Hi</Text>
    </>
  );
}

const styles = StyleSheet.create({
  cameraStyle: {
    position: 'absolute',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
  },
  box: {
    backgroundColor: 'white',
    position: 'absolute',
    top: 20,
    right: 20,
    width: 150,
    height: 150,
    zIndex: 100,
  },
});

// export default class App extends React.Component {
//   // state = {res: 0};

//   async componentDidMount() {
//     // let tmp = await Alt.trigger(10, 3);
//     // console.log(tmp);
//     // this.setState({res: tmp});
//   }

//   render() {
//     return (
//       <View>
//         {/* <Text>Hello ReactNative</Text>
//         <Text>this is a+b from kotlin: {this.state.res}</Text> */}

//       </View>
//     );
//   }
// }
