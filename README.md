# spotify
## Url for test: 
- http://66-228-63-42.ip.linodeusercontent.com/music-event-core/hello
- http://66-228-63-42.ip.linodeusercontent.com/kafka-ui

[![Java CI with Gradle](https://github.com/VitaliyPunko/spotify/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/VitaliyPunko/spotify/actions/workflows/gradle.yml)
Bot configs are in [telegram-bot-secret.yaml]

Kafka UI : http://66-228-63-42.ip.linodeusercontent.com/kafka-ui/ui/clusters/kafka-cluster/all-topics?page=1 

1. Use GitHub action for pushing Docker image to Docker Hub
2. Divide build and push to docker hub GitHub actions to different jobs
3. Set authentication for kafka topic
4. Use lens for kuber

App uses Strimzi for Kafka https://strimzi.io/docs/operators/latest/deploying :

* Strimzi Custom kinds (added by Strimzi operator):
  * Kafka - defines a Kafka cluster
  * KafkaNodePool - defines a group of Kafka nodes
  * KafkaTopic - defines a Kafka topic
  * KafkaUser - defines a Kafka user

1. kind: Kafka - The Cluster Definition
   This is the main Kafka cluster configuration. It defines:
*     How Kafka should behave
*     What features to enable
*     How to connect to it
*     Global settings

`apiVersion: kafka.strimzi.io/v1beta2  # Strimzi API version
kind: Kafka                            # This is a Kafka cluster
metadata:
name: kafka                          # Name of your cluster
namespace: kafka-namespace           # Where it lives
annotations:
strimzi.io/node-pools: enabled     # Use KafkaNodePool (new way)
strimzi.io/kraft: enabled          # Use KRaft mode (no ZooKeeper)`

2. kind: KafkaNodePool — The Node Pool Definition
   This defines a group of Kafka nodes. It defines:
*     How many nodes to have
*     What features to enable
*     How to connect to them
*     Global settings

Todo: change a scheme to my logic  
![img.png](img.png)