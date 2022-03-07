import Reanimated, {runOnJS} from 'react-native-reanimated';
import React, {useEffect, useState} from 'react';
import {View, Text, StyleSheet, Dimensions} from 'react-native';
import {
  useCameraDevices,
  Camera,
  useFrameProcessor,
} from 'react-native-vision-camera';
import Svg, {G, Circle, Polyline, Polygon} from 'react-native-svg';

console.log('in app.js');

export function MoveNet(frame) {
  'worklet';
  return __MoveNet(frame);
}

function SVGgraph({
  points,
  imageWidth,
  imageHeight,
  screenWidth,
  screenHeight,
}) {
  transformPoints(points, imageWidth, imageHeight, screenWidth, screenHeight);

  const onePerson = points[0];
  const r = 7;
  const strokeWidth = 5;
  const circleFill = '#FF9B70';
  const headLeft = '#6beb34';
  const headRight = '#e8eb34';
  const armLeft = '#eb4034';
  const armRight = '#eb8334';
  const legLeft = '#34ebcd';
  const legRight = '#3486eb';
  const body = '#e8eb34';
  const HeadSVG = () => {
    return (
      <G>
        <G>
          <Circle
            class="point4"
            cx={onePerson[4][0]}
            cy={onePerson[4][1]}
            r={r}
            fill={headRight}
          />
          <Circle
            class="point2"
            cx={onePerson[2][0]}
            cy={onePerson[2][1]}
            r={r}
            fill={headRight}
          />
          <Circle
            class="point0"
            cx={onePerson[0][0]}
            cy={onePerson[0][1]}
            r={r}
            fill={headLeft}
          />
          <Circle
            class="point1"
            cx={onePerson[1][0]}
            cy={onePerson[1][1]}
            r={r}
            fill={headLeft}
          />
          <Circle
            class="point3"
            cx={onePerson[3][0]}
            cy={onePerson[3][1]}
            r={r}
            fill={headLeft}
          />
        </G>
        <Polyline
          points={`${onePerson[4][0]},${onePerson[4][1]} ${onePerson[2][0]},${onePerson[2][1]} ${onePerson[0][0]},${onePerson[0][1]} ${onePerson[1][0]},${onePerson[1][1]} ${onePerson[3][0]},${onePerson[3][1]}`}
          fill="none"
          stroke={headLeft}
          strokeWidth={strokeWidth}
        />
      </G>
    );
  };
  const BodySVG = () => {
    return (
      <G>
        <G>
          <Circle
            class="point6"
            cx={onePerson[6][0]}
            cy={onePerson[6][1]}
            r={r}
            fill={body}
          />
          <Circle
            class="point5"
            cx={onePerson[5][0]}
            cy={onePerson[5][1]}
            r={r}
            fill={body}
          />
          <Circle
            class="point11"
            cx={onePerson[11][0]}
            cy={onePerson[11][1]}
            r={r}
            fill={body}
          />
          <Circle
            class="point12"
            cx={onePerson[12][0]}
            cy={onePerson[12][1]}
            r={r}
            fill={body}
          />
        </G>
        <Polygon
          points={`${onePerson[6][0]},${onePerson[6][1]} ${onePerson[5][0]},${onePerson[5][1]} ${onePerson[11][0]},${onePerson[11][1]} ${onePerson[12][0]},${onePerson[12][1]}`}
          fill="none"
          stroke={body}
          strokeWidth={strokeWidth}
        />
      </G>
    );
  };
  const ArmSVG = () => {
    return (
      <G>
        <G>
          <Circle
            class="point6"
            cx={onePerson[6][0]}
            cy={onePerson[6][1]}
            r={r}
            fill={armRight}
          />
          <Circle
            class="point8"
            cx={onePerson[8][0]}
            cy={onePerson[8][1]}
            r={r}
            fill={armRight}
          />
          <Circle
            class="point10"
            cx={onePerson[10][0]}
            cy={onePerson[10][1]}
            r={r}
            fill={armRight}
          />

          <Circle
            class="point5"
            cx={onePerson[5][0]}
            cy={onePerson[5][1]}
            r={r}
            fill={armLeft}
          />
          <Circle
            class="point7"
            cx={onePerson[7][0]}
            cy={onePerson[7][1]}
            r={r}
            fill={armLeft}
          />
          <Circle
            class="point9"
            cx={onePerson[9][0]}
            cy={onePerson[9][1]}
            r={r}
            fill={armLeft}
          />
        </G>
        <Polyline
          points={`${onePerson[6][0]},${onePerson[6][1]} ${onePerson[8][0]},${onePerson[8][1]} ${onePerson[10][0]},${onePerson[10][1]}`}
          fill="none"
          stroke={armRight}
          strokeWidth={strokeWidth}
        />
        <Polyline
          points={`${onePerson[5][0]},${onePerson[5][1]} ${onePerson[7][0]},${onePerson[7][1]} ${onePerson[9][0]},${onePerson[9][1]}`}
          fill="none"
          stroke={armLeft}
          strokeWidth={strokeWidth}
        />
      </G>
    );
  };
  const LegSVG = () => {
    return (
      <G>
        <G>
          <Circle
            class="point12"
            cx={onePerson[12][0]}
            cy={onePerson[12][1]}
            r={r}
            fill={legRight}
          />
          <Circle
            class="point14"
            cx={onePerson[14][0]}
            cy={onePerson[14][1]}
            r={r}
            fill={legRight}
          />
          <Circle
            class="point16"
            cx={onePerson[16][0]}
            cy={onePerson[16][1]}
            r={r}
            fill={legRight}
          />

          <Circle
            class="point11"
            cx={onePerson[11][0]}
            cy={onePerson[11][1]}
            r={r}
            fill={legLeft}
          />
          <Circle
            class="point13"
            cx={onePerson[13][0]}
            cy={onePerson[13][1]}
            r={r}
            fill={legLeft}
          />
          <Circle
            class="point15"
            cx={onePerson[15][0]}
            cy={onePerson[15][1]}
            r={r}
            fill={legLeft}
          />
        </G>
        <Polyline
          points={`${onePerson[12][0]},${onePerson[12][1]} ${onePerson[14][0]},${onePerson[14][1]} ${onePerson[16][0]},${onePerson[16][1]}`}
          fill="none"
          stroke={legRight}
          strokeWidth={strokeWidth}
        />
        <Polyline
          points={`${onePerson[11][0]},${onePerson[11][1]} ${onePerson[13][0]},${onePerson[13][1]} ${onePerson[15][0]},${onePerson[15][1]}`}
          fill="none"
          stroke={legLeft}
          strokeWidth={strokeWidth}
        />
      </G>
    );
  };
  console.log('person0: ', onePerson[0][0], onePerson[0][1]);
  return (
    // screen: w 360 h 719.7
    <Svg style={StyleSheet.absoluteFill}>
      <HeadSVG />
      <BodySVG />
      <ArmSVG />
      <LegSVG />
    </Svg>
  );
}

function transformPoints(
  points,
  imageWidth,
  imageHeight,
  screenWidth,
  screenHeight,
) {
  let ratioX = screenWidth / imageHeight,
    ratioY = screenHeight / imageWidth;
  const onePerson = points[0];
  console.log('sizes: ', imageWidth, imageHeight, screenWidth, screenHeight);
  for (let k = 0; k < onePerson.length; k++) {
    let x = onePerson[k][0],
      y = onePerson[k][1];

    onePerson[k][0] = (imageHeight - y) * ratioX;
    onePerson[k][1] = x * ratioY;
  }
}

export default function App() {
  const devices = useCameraDevices('wide-angle-camera');
  const frontDevice = devices.front; // front camera
  const backDevice = devices.back; // back camera

  const [cameraPermission, setCameraPermission] = useState();
  const [microphonePermission, setMicrophonePermission] = useState();

  const [points, setPoints] = useState([new Array(17).fill([0, 0])]);

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

  return (
    <>
      <Camera
        style={styles.cameraStyle}
        device={frontDevice}
        isActive={true}
        orientation="portraitUpsideDown"
        frameProcessor={frameProcessor}
        frameProcessorFps={5}
      />
      <SVGgraph
        points={points}
        imageWidth={960}
        imageHeight={540}
        screenWidth={360}
        screenHeight={719.67}
      />
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
