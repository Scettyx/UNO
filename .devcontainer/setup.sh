#!/bin/bash
set -e

echo "Setup UNO"

# Installa estensioni per JavaFX
sudo apt-get update -q
sudo apt-get install -y -q \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libx11-6 \
    xvfb \
    x11-utils

# Scarica JavaFX SDK 21
echo "JavaFX è gestito da Maven tramite pom.xml"

# Esegue mvn install per scaricare le estensioni
# mvn dependency:resolve -q

echo "Setup completo"
echo "Per avviare il main: mvn javafx:run"
echo "Per i test: mvn test"