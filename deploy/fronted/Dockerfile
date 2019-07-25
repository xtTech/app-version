FROM node:lts-alpine

# install simple http server for serving static content
RUN npm install -g http-server

# make the 'app' folder the current working directory
WORKDIR /app


# copy project files and folders to the current working directory (i.e. 'app' folder)
COPY ./dist ./dist

EXPOSE 8080
CMD [ "http-server", "dist" ]
