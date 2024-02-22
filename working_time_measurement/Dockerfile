FROM node:20-alpine3.18
WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
COPY ./ ./
CMD ["npm", "install"]
# RUN npm install
EXPOSE 3000
CMD ["npm", "start"]