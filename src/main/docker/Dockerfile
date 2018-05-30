	FROM frolvlad/alpine-oraclejdk8:slim
	VOLUME /tmp

	ADD noser-hackathon-1.jar app.jar

	ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


