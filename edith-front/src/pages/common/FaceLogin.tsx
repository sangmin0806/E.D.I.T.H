import React, { useEffect, useRef, useState } from 'react';
import * as faceapi from '@vladmandic/face-api';

const App: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [status, setStatus] = useState("Face login을 시작합니다.");
  const [webSocket, setWebSocket] = useState<WebSocket | null>(null);

  const EAR_THRESHOLD = 0.29;
  const MIN_DURATION = 100;
  let blinkStart: number | null = null;

  // 모델 로드
  const loadModels = async () => {
    await faceapi.nets.tinyFaceDetector.loadFromUri('/models');
    await faceapi.nets.faceLandmark68Net.loadFromUri('/models');
    await faceapi.nets.faceRecognitionNet.loadFromUri('/models');
  };

  // 카메라 설정
  const setupCamera = async () => {
    if (videoRef.current) {
      const stream = await navigator.mediaDevices.getUserMedia({ video: {} });
      videoRef.current.srcObject = stream;

      return new Promise<void>((resolve) => {
        videoRef.current!.onloadedmetadata = async () => {
          await videoRef.current!.play();
          resolve();
        };
      });
    }
  };

  // 눈 깜빡임 비율 계산
  const calculateEAR = (landmarks: faceapi.Point[], leftEyeIndices: number[], rightEyeIndices: number[]) => {
    const aspectRatio = (eyePoints: faceapi.Point[]) => {
      const A = Math.hypot(eyePoints[1].x - eyePoints[5].x, eyePoints[1].y - eyePoints[5].y);
      const B = Math.hypot(eyePoints[2].x - eyePoints[4].x, eyePoints[2].y - eyePoints[4].y);
      const C = Math.hypot(eyePoints[0].x - eyePoints[3].x, eyePoints[0].y - eyePoints[3].y);
      return (A + B) / (2.0 * C);
    };

    const leftEAR = aspectRatio(leftEyeIndices.map(i => landmarks[i]));
    const rightEAR = aspectRatio(rightEyeIndices.map(i => landmarks[i]));
    return (leftEAR + rightEAR) / 2.0;
  };

  // 정면 얼굴인지 판별
  const isFrontalFace = (landmarks: faceapi.Point[], threshold = 18) => {
    const leftEye = landmarks[36];
    const rightEye = landmarks[45];
    const nose = landmarks[30];
    const leftToNoseDist = Math.hypot(leftEye.x - nose.x, leftEye.y - nose.y);
    const rightToNoseDist = Math.hypot(rightEye.x - nose.x, rightEye.y - nose.y);
    return Math.abs(leftToNoseDist - rightToNoseDist) < threshold;
  };

  // 얼굴 박스 그리기 (좌우 반전 포함)
  const drawFaceBox = (context: CanvasRenderingContext2D, box: faceapi.Box, isFrontal: boolean) => {
    const margin = 0;
    const x = context.canvas.width - (box.x + box.width) - margin;
    const y = box.y + margin;
    const width = box.width - margin;
    const height = Math.min(box.height - margin, box.width * 0.8);

    context.clearRect(0, 0, context.canvas.width, context.canvas.height);
    context.lineWidth = 3;
    context.strokeStyle = isFrontal ? "green" : "red";
    context.strokeRect(x, y, width, height);
  };

  // 서버에 임베딩 데이터 전송
  const sendEmbeddingToServer = (embedding: Float32Array) => {
    if (webSocket && webSocket.readyState === WebSocket.OPEN) {
      webSocket.send(JSON.stringify({ vector: Array.from(embedding) }));
    }
  };

  // 얼굴 인식 및 눈 깜빡임 감지 시작
  const startFaceDetection = () => {
    if (videoRef.current && canvasRef.current) {
      const canvas = canvasRef.current;
      const context = canvas.getContext('2d')!;
      canvas.width = videoRef.current.videoWidth;
      canvas.height = videoRef.current.videoHeight;

      setInterval(async () => {
        const detections = await faceapi.detectAllFaces(videoRef.current!, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceDescriptors();

        context.clearRect(0, 0, canvas.width, canvas.height);

        // 얼굴이 여러 명 감지된 경우 처리
        if (detections.length > 1) {
          setStatus("한명만 카메라에 나와주세요.");
          detections.forEach(detection => drawFaceBox(context, detection.detection.box, false));
          blinkStart = null;
          return;
        }

        // 얼굴이 감지되지 않은 경우
        if (detections.length === 0) {
          setStatus("얼굴이 감지되지 않았습니다.");
          blinkStart = null;
          return;
        }

        const landmarks = detections[0].landmarks.positions;
        const isFrontal = isFrontalFace(landmarks);

        if (!isFrontal) {
          setStatus("정면을 봐주세요~");
          detections.forEach(detection => drawFaceBox(context, detection.detection.box, false));
          blinkStart = null;
          return;
        }

        const ear = calculateEAR(landmarks, [36, 37, 38, 39, 40, 41], [42, 43, 44, 45, 46, 47]);

        if (ear < EAR_THRESHOLD) {
          if (!blinkStart) blinkStart = Date.now();
        } else if (blinkStart && Date.now() - blinkStart >= MIN_DURATION) {
          const embedding = detections[0].descriptor;
          setStatus("서버로 전송 중...");
          sendEmbeddingToServer(embedding); // 서버로 임베딩 데이터 전송
          blinkStart = null;
        } else {
          blinkStart = null;
        }

        drawFaceBox(context, detections[0].detection.box, isFrontal);
      }, 100);
    }
  };

  // 웹소켓 연결 설정
  const connectWebSocket = () => {
    const ws = new WebSocket("wss://edith-ai.xyz:30443/ws/v1/face-recognition/face-login");

    ws.onopen = () => {
      console.log("웹소켓 연결 성공");
      setWebSocket(ws);
    };

    ws.onmessage = (message) => {
      const data = JSON.parse(message.data);
      if (data.userId) {
        setStatus(`로그인 성공! 사용자 ID: ${data.userId}, 유사도 점수: ${data.similarity_score}`);
        ws.close();
      }
    };

    ws.onclose = () => {
      console.log("웹소켓 연결 종료");
      setWebSocket(null);
    };

    ws.onerror = (error) => {
      console.error("웹소켓 오류:", error);
    };
  };

  useEffect(() => {
    const initialize = async () => {
      await loadModels();
      await setupCamera();
      connectWebSocket(); // 웹소켓 연결 시작
      startFaceDetection();
    };
    initialize();
  }, []);

  return (
    <div>
      <h1>Face Recognition Test</h1>
      <div style={{ position: 'relative', width: '600px' }}>
        <video
          ref={videoRef}
          style={{ width: '100%', transform: 'scaleX(-1)' }} // 좌우 반전
          autoPlay
          muted
        />
        <canvas
          ref={canvasRef}
          style={{ position: 'absolute', top: 0, left: 0, transform: 'scaleX(-1)' }} // 좌우 반전
        />
        <p style={{
          position: 'absolute',
          bottom: '10px',
          left: '50%',
          transform: 'translateX(-50%)',
          color: 'white',
          textShadow: '2px 2px 4px rgba(0, 0, 0, 0.5)'
        }}>
          {status}
        </p>
      </div>
    </div>
  );
};

export default App;
