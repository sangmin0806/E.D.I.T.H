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

  const EAR_THRESHOLD = 0.29;
  const MIN_DURATION = 100;
  let blinkStart: number | null = null;

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

  const calculateEAR = (
    landmarks: faceapi.Point[],
    leftEyeIndices: number[],
    rightEyeIndices: number[]
  ) => {
    const aspectRatio = (eyePoints: faceapi.Point[]) => {
      const A = Math.hypot(
        eyePoints[1].x - eyePoints[5].x,
        eyePoints[1].y - eyePoints[5].y
      );
      const B = Math.hypot(
        eyePoints[2].x - eyePoints[4].x,
        eyePoints[2].y - eyePoints[4].y
      );
      const C = Math.hypot(
        eyePoints[0].x - eyePoints[3].x,
        eyePoints[0].y - eyePoints[3].y
      );
      return (A + B) / (2.0 * C);
    };
    const leftEAR = aspectRatio(leftEyeIndices.map((i) => landmarks[i]));
    const rightEAR = aspectRatio(rightEyeIndices.map((i) => landmarks[i]));
    return (leftEAR + rightEAR) / 2.0;
  };

  const isFrontalFace = (landmarks: faceapi.Point[], threshold = 18) => {
    const leftEye = landmarks[36];
    const rightEye = landmarks[45];
    const nose = landmarks[30];
    const leftToNoseDist = Math.hypot(leftEye.x - nose.x, leftEye.y - nose.y);
    const rightToNoseDist = Math.hypot(rightEye.x - nose.x, rightEye.y - nose.y);
    return Math.abs(leftToNoseDist - rightToNoseDist) < threshold;
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

  const sendEmbeddingToServer = async (embedding: Float32Array) => {
    try {
      setStatus("서버로 전송 중...");
      const result = await faceLoginRequest(Array.from(embedding));

      console.log("서버에서 받은 데이터:", result);
      if (result.success && result.response) {
        const {
          name,
          email,
          userId,
          similarity_score,
          profileImageUrl,
          accessToken,
          refreshToken,
          username,
        } = result.response;

        setStatus(`로그인 성공! ${name}님 안녕하세요`);
        stopCamera();

        const userInfo = {
          name,
          email,
          userId,
          similarity_score,
          profileImageUrl,
          username,
        };

        sessionStorage.setItem("userInfo", JSON.stringify(userInfo));
        navigate("/project");
      } else {
        setStatus("로그인 실패");
        setRetryLogin(true);
      }
    } catch (error) {
      console.error("서버 요청 중 오류 발생:", error);
      setStatus("로그인 요청에 실패했습니다.");
      setRetryLogin(true);
    }
  };

  const startFaceDetection = () => {
    if (!videoRef.current || !canvasRef.current) {
      console.error("비디오 또는 캔버스 요소가 초기화되지 않았습니다.");
      setStatus("카메라를 사용할 수 없습니다.");
      return;
    }

    const canvas = canvasRef.current;
    const context = canvas.getContext("2d")!;
    canvas.width = videoRef.current.videoWidth;
    canvas.height = videoRef.current.videoHeight;

    const intervalId = setInterval(async () => {
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
        blinkStart = null;
        return;
      }

      const landmarks = detections[0].landmarks.positions;
      const isFrontal = isFrontalFace(landmarks);

      if (!isFrontal) {
        setStatus("정면을 봐주세요~");
        drawFaceBox(context, detections[0].detection.box, false);
        blinkStart = null;
        return;
      }

      const ear = calculateEAR(
        landmarks,
        [36, 37, 38, 39, 40, 41],
        [42, 43, 44, 45, 46, 47]
      );

      if (ear < EAR_THRESHOLD) {
        if (!blinkStart) blinkStart = Date.now();
      } else if (blinkStart && Date.now() - blinkStart >= MIN_DURATION) {
        const embedding = detections[0].descriptor;
        sendEmbeddingToServer(embedding);
        blinkStart = null;
      } else {
        blinkStart = null;
      }

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

    // 클린업 함수: 언마운트 시 실행
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
      <img
        src={mainRight}
        className="absolute right-0 top-0 translate-y-8 w-32 z-0"
        alt="Right Image"
      />
      <img
        src={mainLeft}
        className="absolute left-[150px] top-150 w-40vh max-h-[100vh] z-0"
      />
      <div className="min-h-[100vh] flex flex-col justify-center items-center z-10">
        <div className="flex flex-col items-center px-12 py-16 bg-white/60 rounded-3xl shadow border border-black justify-center gap-6 z-20">
          <h1>얼굴 인식 로그인</h1>
          <div style={{ position: "relative", width: "600px" }}>
            <video
              ref={videoRef}
              style={{ width: "100%", transform: "scaleX(-1)" }}
              autoPlay
              muted
            />
            <canvas
              ref={canvasRef}
              style={{ position: "absolute", top: 0, left: 0 }}
            />
            <p
              style={{
                position: "absolute",
                bottom: "10px",
                left: "50%",
                transform: "translateX(-50%)",
                color: "white",
                textShadow: "2px 2px 4px rgba(0, 0, 0, 0.5)",
              }}
            >
              {status}
            </p>
          </div>
        </div>
      </div>
    </>
  );
};

export default App;
