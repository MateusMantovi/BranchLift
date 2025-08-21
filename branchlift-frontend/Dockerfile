# Estágio 1: Build
FROM node:20-slim as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration=production

# Estágio 2: Serve
FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/branchlift-frontend/browser /usr/share/nginx/html

# --- LINHAS DE CORREÇÃO ---
# Apaga o index.html padrão do Nginx que pode ter sobrado
RUN rm /usr/share/nginx/html/index.html
# Renomeia o arquivo principal do Angular para o nome que o Nginx espera
RUN mv /usr/share/nginx/html/index.csr.html /usr/share/nginx/html/index.html
# --------------------------

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
