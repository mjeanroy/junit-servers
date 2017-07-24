#!/bin/bash

function info {
  echo "[info] $1"
}

IMG="mjeanroy/junit-servers"
NAME="website-junit-servers"
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

info "Starting Jekyll images to run website"
info "Current working directory: ${DIR}"
info "Checking for local image"

if [[ "$(docker images -q ${IMG} 2> /dev/null)" == "" ]]; then
  info "Create docker image ${IMG}"
  docker build -t ${IMG} ${DIR}
fi

if [[ "$(docker ps -a -q -f name=^/${NAME}$ 2> /dev/null)" == "" ]]; then
  info "Running docker image ${IMG}"
  docker run -d -p 4000:4000 -v ${DIR}:/srv/jekyll --name ${NAME} ${IMG}
else
  if [[ "$(docker ps -q -f name=^/${NAME}$ 2> /dev/null)" == "" ]]; then
    info "Starting existing container ${NAME}"
    docker start ${NAME}
  else
    info "Container ${NAME} already started"
  fi
fi
