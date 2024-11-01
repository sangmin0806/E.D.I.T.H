import axios from 'axios';


const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true  
});

// 요청 인터셉터 
axiosInstance.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // 토큰 만료 시 상태 코드와 헤더 확인
    if (error.response.status === 401 && error.response.headers['Token-Status'] === 'expired' && !originalRequest._retry) {
      originalRequest._retry = true;
      try {

        await axios.post('/users/token/refresh', {}, { withCredentials: true });

        return axiosInstance(originalRequest);
      } catch (refreshError) {
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;