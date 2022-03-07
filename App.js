// import 'react-native-reanimated';
import Reanimated, {runOnJS} from 'react-native-reanimated';
import React, {useEffect, useState} from 'react';
import {View, Text, StyleSheet, Dimensions } from 'react-native';
// import InterfaceMovenet from './kotlin';
import {
  useCameraDevices,
  Camera,
  useFrameProcessor,
} from 'react-native-vision-camera';
import Svg, {G, Circle, Polyline, Polygon, Line} from 'react-native-svg';

console.log('in app.js');

export function MoveNet(frame) {
  'worklet';
  return __MoveNet(frame);
}

function getFrameSize(frameWidth, frameHeight) {
  let width, height;
  if (frameWidth > frameHeight && Dimensions.get('window').width>Dimensions.get('window').height){
    width = frameWidth;
    height = frameHeight;
  }else {
    console.log("Has rotation");
    width = frameHeight;
    height = frameWidth;
  }
  return [width, height];
} 


function SVGgraph({points, frameWidth, frameHeight}) {
  // console.log(points);
  const frameSize = getFrameSize(frameWidth, frameHeight);
  const viewBox = `0 0 ${frameSize[0]} ${frameSize[1]}`;

  const onePerson = points[0];
  const r = 10;
  const circleFill = "#FF9B70";
  const headcircleFill = "green";
  const bodycircleFill = "red";
  const armcircleFill = "blue";
  const legcircleFill = "yellow";
  const HeadSVG = () => {
    return (
      <G>
        <G>
          <Circle class="point4" cx={onePerson[4][0]} cy={onePerson[4][1]} r={r} fill={headcircleFill}/>
          <Circle class="point2" cx={onePerson[2][0]} cy={onePerson[2][1]} r={r} fill={headcircleFill}/>
          <Circle class="point0" cx={onePerson[0][0]} cy={onePerson[0][1]} r={r} fill={headcircleFill}/>
          <Circle class="point1" cx={onePerson[1][0]} cy={onePerson[1][1]} r={r} fill={headcircleFill}/>
          <Circle class="point3" cx={onePerson[3][0]} cy={onePerson[3][1]} r={r} fill={headcircleFill}/>
        </G>
        <Polyline points={``}/>
      </G>
    );
  }
  const BodySVG = () => {
    return (
      <G>
        <G>
          <Circle class="point6" cx={onePerson[6][0]} cy={onePerson[6][1]} r={r} fill={bodycircleFill}/>
          <Circle class="point5" cx={onePerson[5][0]} cy={onePerson[5][1]} r={r} fill={bodycircleFill}/>
          <Circle class="point11" cx={onePerson[11][0]} cy={onePerson[11][1]} r={r} fill={bodycircleFill}/>
          <Circle class="point12" cx={onePerson[12][0]} cy={onePerson[12][1]} r={r} fill={bodycircleFill}/>
        </G>
        <Polygon points={``}/>
      </G>
    );
  }
  const ArmSVG = () => {
    return (
      <G>
        <G>
          <Circle class="point6" cx={onePerson[6][0]} cy={onePerson[6][1]} r={r} fill={armcircleFill}/>
          <Circle class="point8" cx={onePerson[8][0]} cy={onePerson[8][1]} r={r} fill={armcircleFill}/>
          <Circle class="point10" cx={onePerson[10][0]} cy={onePerson[10][1]} r={r} fill={armcircleFill}/>

          <Circle class="point5" cx={onePerson[5][0]} cy={onePerson[5][1]} r={r} fill={armcircleFill}/>
          <Circle class="point7" cx={onePerson[7][0]} cy={onePerson[7][1]} r={r} fill={armcircleFill}/>
          <Circle class="point9" cx={onePerson[9][0]} cy={onePerson[9][1]} r={r} fill={armcircleFill}/>
        </G>
        <Polyline points={``}/>
        <Polyline points={``}/>
      </G>
    );
  }
  const LegSVG = () => {
    return (
      <G>
        <G>
          <Circle class="point12" cx={onePerson[12][0]} cy={onePerson[12][1]} r={r} fill={legcircleFill}/>
          <Circle class="point14" cx={onePerson[14][0]} cy={onePerson[14][1]} r={r} fill={legcircleFill}/>
          <Circle class="point16" cx={onePerson[16][0]} cy={onePerson[16][1]} r={r} fill={legcircleFill}/>

          <Circle class="point11" cx={onePerson[11][0]} cy={onePerson[11][1]} r={r} fill={legcircleFill}/>
          <Circle class="point13" cx={onePerson[13][0]} cy={onePerson[13][1]} r={r} fill={legcircleFill}/>
          <Circle class="point15" cx={onePerson[15][0]} cy={onePerson[15][1]} r={r} fill={legcircleFill}/>
        </G>
        <Polyline d={``}/>
        <Polyline d={``}/>
      </G>
    );
  }
  console.log("person0: ", onePerson[0][0], onePerson[0][1]);
  return (
    // screen: w 360 h 719.7
    <Svg width="360" height={719.666} viewBox={viewBox}>
      {/* <HeadSVG />
      <BodySVG />
      <ArmSVG />
      <LegSVG /> */}
      <G rotation={-90} translateY={960} origin="0,0">
          <Circle class="point4" cx={onePerson[4][0]} cy={onePerson[4][1]} r={r} fill={"red"}/>
          <Circle class="point2" cx={onePerson[2][0]} cy={onePerson[2][1]} r={r} fill={"orange"}/>
          <Circle class="point0" cx={onePerson[0][0]} cy={onePerson[0][1]} r={r} fill={"yellow"}/>
          <Circle class="point1" cx={onePerson[1][0]} cy={onePerson[1][1]} r={r} fill={"green"}/>
          <Circle class="point3" cx={onePerson[3][0]} cy={onePerson[3][1]} r={r} fill={"blue"}/>
        <Circle cx="0" cy="0" r={10} fill="blue"/>
        {/* <Line x1={0} y1={0} x2={frameSize[0]} y2={frameSize[1]} stroke="red" strokeWidth="2" /> */}
        <Line x1={0} y1={0} x2="960" y2="540" stroke="red" strokeWidth="2" />
      </G>
    </Svg>
  );
}

export default function App() {
  const devices = useCameraDevices('wide-angle-camera');
  const frontDevice = devices.front; // front camera
  const backDevice = devices.back; // back camera

  const [cameraPermission, setCameraPermission] = useState();
  const [microphonePermission, setMicrophonePermission] = useState();

  // const [frameHeight, setFrameHeight] = useState();
  // const [frameWidth, setFrameWidth] = useState();
  const [layoutWidth, setLayoutWidth] = useState();
  const [layoutHeight, setLayoutHeight] = useState();
  const [points, setPoints] = useState([new Array(17).fill([0,0])]);


  const frameProcessor = useFrameProcessor(frame => {
    // frame: w 960 h 540
    'worklet';
    console.log('[-----------FRAME-----------]');
    let result = MoveNet(frame);
    runOnJS(setPoints)(result);
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

  const handleCameraLayout = (event) => {
    const { x, y, width, height } = event.nativeEvent.layout;
    setLayoutHeight(height);
    setLayoutWidth(width);
    console.log("camera size: ", width ,height);
  };

  return (
    <>
      <Camera
        style={styles.cameraStyle}
        device={frontDevice}
        isActive={true}
        // orientation="portraitUpsideDown"
        onLayout={handleCameraLayout}
        frameProcessor={frameProcessor}
        frameProcessorFps={1}
      />
      <SVGgraph points={points} frameWidth={960} frameHeight={540} />
      {/* <Text style={styles.box}>Hi</Text> */}
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
