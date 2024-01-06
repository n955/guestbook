FROM openjdk:8-alpine

ARG VERSION

COPY target/guestbook-0.0.1-SNAPSHOT.jar /app/guestbook.jar

LABEL maintainer="HwanYeoul Jeong<coordinatorj@jadecross.com>" \
      title="Guestbook App" \
      version="$VERSION" \
      description="This image is guestbook service"

ENV APP_HOME /app
EXPOSE 8080
VOLUME /app/upload

WORKDIR $APP_HOME
ENTRYPOINT ["java"]
CMD ["-jar", "guestbook.jar"]
