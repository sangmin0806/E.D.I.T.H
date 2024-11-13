// Registration.tsx
import React, { useEffect, useRef, useState } from "react";
import * as faceapi from "@vladmandic/face-api";
import { faceRegisterRequest } from "../../api/userApi";

const Registration: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [status, setStatus] = useState("아래의 버튼을 눌러 사진 촬영을 시작합니다...");
  const [isRegistering, setIsRegistering] = useState(false);
  const [imageCount, setImageCount] = useState(0);
  const [embeddings, setEmbeddings] = useState<number[]>([]);
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

  // 얼굴 사진 캡처
  const captureImages = async () => {
    if (!videoRef.current) return;
    const collectedEmbeddings: number[][] = [];
    captureInterval.current = setInterval(async () => {
      if (videoRef.current) {
        const detections = await faceapi
          .detectAllFaces(videoRef.current, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceDescriptors();

        console.log("검출된 얼굴 수:", detections.length);

        if (detections.length === 1) {
          const embedding = Array.from(detections[0].descriptor);
          console.log("추출된 임베딩:", embedding);

          if (embedding) {
            console.log(`임베딩 (사진 ${imageCount + 1}):`, embedding);

            setImageCount((prevCount) => {
              const newCount = prevCount + 1;
              setStatus(`사진 ${newCount}장 찍음`);

              if (newCount >= 10) {
                stopCapture();
                setStatus("회원가입 완료! 10장의 사진을 저장했습니다.");
                sendEmbeddingsToServer(collectedEmbeddings); // 모든 사진 촬영 후 서버로 전송
              }
              return newCount;
            });

            collectedEmbeddings.push(embedding);
          }
        }
      }
    }, 1000); // 1초마다 캡처
  };

  // 캡처 중지
  const stopCapture = () => {
    if (captureInterval.current) {
      clearInterval(captureInterval.current);
      captureInterval.current = null;
    }
    setIsRegistering(false);
  };

  // 얼굴 임베딩 서버 전송
  const sendEmbeddingsToServer = async (embeddings: number[][]) => {
    setStatus("임베딩 데이터를 서버에 전송 중...");
    try {
        console.log(embeddings)
      const response = await faceRegisterRequest({
        embeddingVectors: embeddings,
      });
      if (response.success) {
        setStatus("임베딩 데이터가 서버에 성공적으로 전송되었습니다.");
      } else {
        setStatus("임베딩 데이터 전송에 실패했습니다. 다시 시도해 주세요.");
      }
    } catch (error) {
      setStatus("임베딩 데이터 전송에 실패했습니다. 다시 시도해 주세요.");
      console.error("임베딩 전송 에러:", error);
    }
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
