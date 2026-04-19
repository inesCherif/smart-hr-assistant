FROM node:18-alpine

WORKDIR /app

# Efficient caching of npm install step
COPY package.json package-lock.json* ./
RUN npm install

# Copy all UI source files
COPY . .

EXPOSE 3000

# Execute Vite inside the docker container listening publicly mapping to 3000
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "3000"]
