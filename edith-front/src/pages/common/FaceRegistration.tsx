import React, { useEffect, useRef, useState } from "react";
import * as faceapi from "@vladmandic/face-api";
import mainLeft from "../../assets/main_left.png";
import mainRight from "../../assets/main_right.png";
import { faceRegisterRequest } from "../../api/userApi";

const Registration: React.FC = () => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const [status, setStatus] = useState(
    "아래의 버튼을 눌러 사진 촬영을 시작합니다"
  );
  const [isRegistering, setIsRegistering] = useState(false);
  const [imageCount, setImageCount] = useState(0);
  const [embeddings, setEmbeddings] = useState<number[][]>([]);
  const captureInterval = useRef<NodeJS.Timeout | null>(null);
  const [isTakingPhoto, setIsTakingPhoto] = useState(false);

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
          .detectAllFaces(
            videoRef.current,
            new faceapi.TinyFaceDetectorOptions()
          )
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

              // 페이드 인/아웃 효과 설정
              setIsTakingPhoto(true);
              setTimeout(() => setIsTakingPhoto(false), 500);

              if (newCount >= 5) {
                // 5장 촬영 후 중지
                stopCapture();
                setStatus("회원가입 완료! 5장의 사진을 저장했습니다.");
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
    <>
      <img
        src={mainRight}
        className="absolute right-0 top-0 translate-y-8 w-32 z-0" // absolute로 위치 조정
        alt="Right Image"
      />
      <img
        src={mainLeft}
        className="absolute left-[150px] top-150 w-40vh max-h-[100vh] z-0"
      />
      <div className="min-h-[100vh] flex flex-col justify-center items-center z-10">
        <div className="flex flex-col items-center px-12 py-16 bg-white/60 rounded-3xl shadow border border-black justify-center gap-6 z-20">
          <h1>얼굴인식 로그인 등록</h1>
          <div
            style={{ position: "relative", width: "100%", maxWidth: "600px" }}
            className="flex flex-col items-center gap-4"
          >
            <video
              ref={videoRef}
              autoPlay
              muted
              style={{ width: "100%" }}
              className="z-20"
            ></video>

            {/* 촬영 시 페이드 인/아웃 효과 */}
            {isTakingPhoto && (
              <div
                style={{
                  position: "absolute",
                  top: 0,
                  left: 0,
                  width: "100%",
                  height: "100%",
                  backgroundColor: "rgba(255, 255, 255, 0.8)",
                  zIndex: 10,
                  animation: "fadeEffect 0.5s ease-in-out",
                }}
              />
            )}

            <p className="font-medium text-base">{status}</p>
            <button
              onClick={() => !isRegistering && startRegistration()}
              disabled={isRegistering}
              className="px-3 py-1 items-center justify-center bg-black rounded-3xl text-white text-base font-medium"
            >
              {isRegistering ? "얼굴등록 진행 중..." : "얼굴등록 시작"}
            </button>
          </div>

          <style>
            {`
          @keyframes fadeEffect {
            0% { opacity: 0; }
            50% { opacity: 1; }
            100% { opacity: 0; }
          }
        `}
          </style>
        </div>
      </div>
    </>
  );
};

export default Registration;
