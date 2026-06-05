# 第一階段：使用 Maven 進行編譯與打包 (Build Stage)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# 複製 pom.xml 並下載依賴（利用 Docker 快取加速未來的編譯）
COPY pom.xml .
RUN mvn dependency:go-offline

# 複製所有原始碼並打包成 jar 檔
COPY src ./src
RUN mvn clean package -DskipTests

# 第二階段：使用輕量級的 JRE 運行程式 (Run Stage)
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# 從編譯階段把包好的 jar 檔拿過來
COPY --from=build /app/target/*.jar app.jar

# 暴露 Spring Boot 預設的 8080 Port
EXPOSE 8080

# 啟動指令
ENTRYPOINT ["java", "-jar", "app.jar"]