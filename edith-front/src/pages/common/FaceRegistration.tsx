import React, { useEffect, useRef, useState } from "react";
import * as faceapi from "@vladmandic/face-api";

const Registration: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [status, setStatus] = useState("아래의 버튼을 눌러 사진 촬영을 시작합니다...");
  const [isRegistering, setIsRegistering] = useState(false);
  const [imageCount, setImageCount] = useState(0);
  const [embeddings, setEmbeddings] = useState<Float32Array[]>([]);
  const captureInterval = useRef<NodeJS.Timeout | null>(null);

  // 카메라 설정
  const setupCamera = async () => {
    if (videoRef.current) {
      const stream = await navigator.mediaDevices.getUserMedia({ video: {} });
      videoRef.current.srcObject = stream;
      await videoRef.current.play();
    }
  };

  // 모델 로드
  const loadModels = async () => {
    console.log("모델 로딩 시작...");
    await faceapi.nets.tinyFaceDetector.loadFromUri("/models");
    await faceapi.nets.faceLandmark68Net.loadFromUri("/models");
    await faceapi.nets.faceRecognitionNet.loadFromUri("/models");
    console.log("모델 로딩 완료!");
  };

  // 얼굴 캡처
  const captureImages = async () => {
    if (!videoRef.current) return;

    captureInterval.current = setInterval(async () => {
      if (videoRef.current) {
        const detections = await faceapi
          .detectAllFaces(videoRef.current, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceDescriptors();

        // 검출된 얼굴 수 확인 및 상태 업데이트
        if (detections.length === 1) {
          const embedding = detections[0].descriptor;
          setEmbeddings((prev) => [...prev, embedding]);
          setImageCount((prev) => prev + 1);
          setStatus(`사진 ${imageCount + 1}장 촬영 완료`);

          if (imageCount + 1 >= 10) {
            stopCapture();
            setStatus("얼굴등록 완료!");
          }
        }
      }
    }, 1000);
  };

  // 촬영 중지
  const stopCapture = () => {
    if (captureInterval.current) clearInterval(captureInterval.current);
    setIsRegistering(false);
  };

  // 등록 시작
  const startRegistration = async () => {
    setIsRegistering(true);
    setStatus("얼굴등록 중...");
    await loadModels();
    await setupCamera();
    captureImages();
  };

  return (
    <div>
      <h1>회원가입: 얼굴 등록</h1>
      <video ref={videoRef} autoPlay muted style={{ width: "100%" }}></video>
      <p>{status}</p>
      <button onClick={() => !isRegistering && startRegistration()} disabled={isRegistering}>
        {isRegistering ? "얼굴등록 진행 중..." : "얼굴등록 시작"}
      </button>
    </div>
  );
};

export default Registration;
