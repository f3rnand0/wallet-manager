FROM amazoncorretto:11.0.15
WORKDIR /app
EXPOSE 8080
ADD ./build/libs/wallet-manager-1.0.0.jar .
CMD ["sh", "-c", "DATABASE_URL=jdbc:h2:file:~/walletmanager DATABASE_USERNAME=sa DATABASE_PASSWORD= java -jar wallet-manager-1.0.0.jar"]