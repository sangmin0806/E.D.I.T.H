import React, { useEffect, useRef, useState } from "react";
import * as faceapi from "@vladmandic/face-api";
import { useNavigate } from "react-router-dom";

const App: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [status, setStatus] = useState("Face login을 시작합니다.");
  const [webSocket, setWebSocket] = useState<WebSocket | null>(null);
  const navigate = useNavigate();
  const EAR_THRESHOLD = 0.29;
  const MIN_DURATION = 100;
  const isMounted = useRef(true);

  const loadModels = async () => {
    try {
      await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
      await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
      await faceapi.nets.faceRecognitionNet.loadFromUri("/models");
      console.log("모델 로드 완료");
    } catch (error) {
      console.error("모델 로드 실패:", error);
    }
  };

  const setupCamera = async () => {
    try {
      if (videoRef.current) {
        const stream = await navigator.mediaDevices.getUserMedia({ video: {} });
        videoRef.current.srcObject = stream;
        await videoRef.current.play();
      }
    } catch (error) {
      console.error("카메라 설정 실패:", error);
    }
  };

  const stopCamera = () => {
    if (videoRef.current && videoRef.current.srcObject) {
      (videoRef.current.srcObject as MediaStream)
        .getTracks()
        .forEach((track) => track.stop());
      videoRef.current.srcObject = null;
    }
  };

  const startFaceDetection = () => {
    if (videoRef.current && canvasRef.current) {
      const canvas = canvasRef.current;
      const context = canvas.getContext("2d")!;
      canvas.width = videoRef.current.videoWidth;
      canvas.height = videoRef.current.videoHeight;

      const intervalId = setInterval(async () => {
        try {
          const detections = await faceapi
            .detectAllFaces(
              videoRef.current!,
              new faceapi.TinyFaceDetectorOptions()
            )
            .withFaceLandmarks()
            .withFaceDescriptors();

          context.clearRect(0, 0, canvas.width, canvas.height);

          if (detections.length !== 1) {
            setStatus(
              detections.length === 0
                ? "얼굴이 감지되지 않았습니다."
                : "한명만 카메라에 나와주세요."
            );
            return;
          }

          const landmarks = detections[0].landmarks.positions;
          const isFrontal = Math.abs(
            landmarks[36].x - landmarks[45].x
          ) > EAR_THRESHOLD;

          if (!isFrontal) {
            setStatus("정면을 봐주세요~");
            return;
          }

          const embedding = detections[0].descriptor;
          setStatus("서버로 전송 중...");
          sendEmbeddingToServer(embedding);
        } catch (error) {
          console.error("얼굴 감지 실패:", error);
        }
      }, 1000);

      return () => clearInterval(intervalId);
    }
  };

  const sendEmbeddingToServer = (embedding: Float32Array) => {
    if (webSocket && webSocket.readyState === WebSocket.OPEN) {
      webSocket.send(JSON.stringify({ vector: Array.from(embedding) }));
    } else {
      console.error("웹소켓 연결 안 됨");
    }
  };

  const connectWebSocket = () => {
    const ws = new WebSocket(
      "wss://edith-ai.xyz:30443/ws/v1/face-recognition/face-login"
    );

    ws.onopen = () => {
      console.log("웹소켓 연결 성공");
      setWebSocket(ws);
    };

    ws.onmessage = (message) => {
      const data = JSON.parse(message.data);
      if (!isMounted.current) return;

      if (data.success) {
        setStatus("로그인 성공!");
        sessionStorage.setItem("userInfo", JSON.stringify(data.response));
        stopCamera();
        ws.close();
        navigate("/project");
      } else {
        setStatus("로그인 실패. 다시 시도해주세요.");
      }
    };

    ws.onerror = (error) => {
      console.error("웹소켓 오류:", error);
    };

    ws.onclose = () => {
      console.log("웹소켓 연결 종료");
      setWebSocket(null);
    };

    return ws;
  };

  useEffect(() => {
    isMounted.current = true;

    const initialize = async () => {
      await loadModels();
      await setupCamera();
      const ws = connectWebSocket();
      setWebSocket(ws);
    };

    initialize();

    return () => {
      isMounted.current = false;
      stopCamera();
      if (webSocket) webSocket.close();
    };
  }, []);

  return (
    <div className="min-h-screen flex flex-col justify-center items-center">
      <div className="relative w-[600px]">
        <video ref={videoRef} className="w-full h-auto" autoPlay muted />
        <canvas
          ref={canvasRef}
          className="absolute top-0 left-0 w-full h-full"
        ></canvas>
        <p className="text-center mt-4">{status}</p>
      </div>
    </div>
  );
};

export default App;
