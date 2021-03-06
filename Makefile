.PHONY : clean default dockerimage

SHELL = /bin/bash

default: dockerimage

# this docker command uses the host user's .m2 directory
# to cache the maven artifacts for repeated builds
#
# it creates the .war file in packaging/docker/
packaging/docker/metalnx.war:
	docker run -it --rm \
		-v "$$PWD":/usr/src \
		-v "$$PWD"/src:/usr/src/mymaven \
		-v "$$HOME/.m2":/root/.m2 \
		-w /usr/src/mymaven \
		maven:3-jdk-8-alpine \
		mvn package -Dmaven.test.skip=true

# this docker command builds a local docker image
dockerimage: packaging/docker/metalnx.war
	docker build -t myimages/metalnx:latest .

# this removes the .war file
# it does not clean maven's .m2 cache
# it does not remove the docker image
clean:
	rm -f packaging/docker/metalnx.war
