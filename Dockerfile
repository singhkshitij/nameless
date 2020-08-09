FROM clojure:openjdk-8-lein-slim-buster
ENV dbconfig "{}"
ENV hikariconfig "{}"
RUN mv "$(lein with-profile prod uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" nameless.jar
COPY nameless.jar /usr/src/app
COPY run.sh /usr/src/app
WORKDIR /usr/src/app
ENTRYPOINT ["sh", "-c", "java","-Ddb=${dbconfig}","-Dhikari=${hikariconfig}", "-jar", "nameless.jar", "server"]
EXPOSE 8080
