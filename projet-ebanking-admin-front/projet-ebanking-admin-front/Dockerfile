# Étape 1 : Builder l'application Angular
FROM node:22.5.1-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production --project=admin-front

# Étape 2 : Servir l'app avec NGINX
FROM nginx:alpine
RUN rm -rf /usr/share/nginx/html/*
COPY --from=builder /app/dist/admin-front/browser  /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
