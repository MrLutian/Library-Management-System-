FROM tomcat:10.1-jdk17-temurin
RUN apt-get update && apt-get install -y --no-install-recommends maven && rm -rf /var/lib/apt/lists/*
WORKDIR /workspace
COPY . /workspace
EXPOSE 8080
CMD ["bash", "-lc", "until (echo > /dev/tcp/db/3306) >/dev/null 2>&1; do echo '等待 MySQL 启动...'; sleep 2; done; mvn -B -DskipTests package && cp target/library-management.war /usr/local/tomcat/webapps/ROOT.war && catalina.sh run"]
