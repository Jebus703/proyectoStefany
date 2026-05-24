# ===========================================
# Multi-stage Dockerfile para Angular
# ===========================================

# Etapa 1: Build
FROM node:20-alpine AS build

WORKDIR /app

# Copiar package.json primero (para cachear dependencias)
COPY package*.json ./

# Instalar dependencias
RUN npm ci

# Copiar el código fuente
COPY . .

# Compilar la aplicación
RUN npm run build

# ===========================================
# Etapa 2: Runtime con Nginx
# ===========================================
FROM nginx:alpine

# Copiar configuración de nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Copiar los archivos compilados desde la etapa de build
COPY --from=build /app/dist/front/browser /usr/share/nginx/html

# Exponer el puerto
EXPOSE 80

# Comando de inicio
CMD ["nginx", "-g", "daemon off;"]
