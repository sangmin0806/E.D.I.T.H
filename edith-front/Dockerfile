FROM node:18 AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN rm -rf node_modules/ .vite
RUN rm -rf node_modules
RUN npm install
COPY . .

RUN npm run build

FROM nginx:alpine
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
EXPOSE 443

CMD ["nginx", "-g", "daemon off;"]