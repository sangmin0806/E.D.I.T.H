import React, { useEffect, useRef, useState } from "react";
import * as faceapi from "@vladmandic/face-api";
import { useNavigate } from "react-router-dom";
import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";
import { faceLoginRequest } from "../../api/userApi";

const App: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [status, setStatus] = useState("Face login을 시작합니다.");
  const [retryLogin, setRetryLogin] = useState(false);
  const [faceDetectionInterval, setFaceDetectionInterval] = useState<number | null>(null);
  const navigate = useNavigate();

  const loadModels = async () => {
    await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
    await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
    await faceapi.nets.faceRecognitionNet.loadFromUri("/models");
  };

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

  const stopCamera = () => {
    if (videoRef.current && videoRef.current.srcObject) {
      (videoRef.current.srcObject as MediaStream)
        .getTracks()
        .forEach((track) => track.stop());
      videoRef.current.srcObject = null;
    }

    if (faceDetectionInterval) {
      clearInterval(faceDetectionInterval);
      setFaceDetectionInterval(null);
    }
  };

  const drawFaceBox = (
    context: CanvasRenderingContext2D,
    box: faceapi.Box,
    isFrontal: boolean
  ) => {
    const x = context.canvas.width - (box.x + box.width);
    const y = box.y;
    const width = box.width;
    const height = Math.min(box.height, box.width * 0.8);

    context.clearRect(0, 0, context.canvas.width, context.canvas.height);
    context.lineWidth = 3;
    context.strokeStyle = isFrontal ? "green" : "red";
    context.strokeRect(x, y, width, height);
  };

  const startFaceDetection = () => {
    if (!videoRef.current || !canvasRef.current) {
      console.error("비디오 또는 캔버스 요소가 초기화되지 않았습니다.");
      return;
    }

    const canvas = canvasRef.current;
    const context = canvas.getContext("2d")!;
    console.log("videoRef.current 상태:", videoRef.current);
    console.log("canvasRef.current 상태:", canvasRef.current);

    canvas.width = videoRef.current.videoWidth;
    canvas.height = videoRef.current.videoHeight;

    const intervalId = setInterval(async () => {
      if (!videoRef.current) {
        console.error("비디오 요소가 유효하지 않습니다.");
        clearInterval(intervalId);
        return;
      }

      const detections = await faceapi
        .detectAllFaces(
          videoRef.current,
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
      const isFrontal = true; // 정면 여부 체크 로직 추가 가능
      drawFaceBox(context, detections[0].detection.box, isFrontal);
    }, 100);

    setFaceDetectionInterval(intervalId as unknown as number);
  };

  useEffect(() => {
    const initialize = async () => {
      try {
        await loadModels();
        await setupCamera();
        startFaceDetection();
      } catch (error) {
        console.error("초기화 중 오류 발생:", error);
        setStatus("초기화 중 문제가 발생했습니다.");
      }
    };

    initialize();

    return () => {
      console.log("리소스 정리 중...");
      stopCamera();
    };
  }, []);

  useEffect(() => {
    if (retryLogin) {
      startFaceDetection();
      setRetryLogin(false);
    }
  }, [retryLogin]);

  return (
    <>
      <div className="app-container">
        <video ref={videoRef} autoPlay muted style={{ display: "none" }} />
        <canvas ref={canvasRef} />
        <p>{status}</p>
      </div>
    </>
  );
};

export default App;
