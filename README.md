# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+uB5bEaR3xqTsNFoTp8K+s6XYwAgQnihignCQSRJgKSb6GLuNL7gyTJTm617Dg6S7CjAADiTIbrAcqRR6KrBhqAByECRdqur6qoFbKYYjwwMZAI3l5d5JpZnbdr2-buSB1T+gAQiGzlqGAUYxnGhRacVolpk4ACMBE5hl+bQUWJb1D40yXtASAAF4oLsdFNoFi7iVQSIxVulWebyI5jhOKDPvEGL+doc6FUFlQPmuAaXh6i2qMV7krtd61We25n1D+uJQBkqgAZg5lVRJYGEfp8wkah3wUVR9aGZp1XwsmyCpjAuH4aMwPvJBYMweRl5Q8hWNofRpieN4fj+F4KDoDEcSJBTVOOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKcMkOIeg6EWQ8sK6WMIvUQTDbmRUD1UCaSBaOiGIAGqq+rKANSS24oBUt21IyYD7Ri+14ydW38hUy4ZBYqA0MaaDGhNUDTXk8ZxeqMBJTAP0gPqsti7d91WbUPZ9obEeA7UdUcPrTXRigsYKeLFSdQAkOmvWjMs-V5vMQ3FtAo3jXqk0zbsMDzQx4dK5H+2vlZxVvS3W7-ctq22fYjMOUJjPJ25L2bQuo5GCg3DHpe5646LaA2xP94hdI09MtlC9ITApjd8tumw4DmGI9hyN4Vm9eMaTAQouu-jYOKGr8WiYVKhozMH6WDShdzfP2EqYW28xbtUqG9GA4xQ74xhn9R4cIm6lRVsgXWWsdZyD1igQkjVR6dmNrefkpsmQWytovZe+57bCkdp7FALt2Duyrp7Ka3tCi+0SslY4YBg7ikcFAgqtslolRWi6GA0cKpjy-vUROydmpp1auLE+JQwBdXztmfkxcaLDXLgqSu8Rq6zQbETRu5QHqd3kLHCoHcnpmO7oI3uaIHJohHobSkJsbJolCu-DEZCirnUoUyahtC+wwAATmPhK9iodiEdZDxOZW6dlenA-0L8cjfV+vvQRullghLUAWBo4xskAElpAFm6uEYIgQQSbHiLqFAbpOR7G+MkUAao6mQUWN8bJCUlTtIuDAToR9EwI0UbUFGWYsnv1yfkpURSSllIqcsKpNTWmfHaSCJpIAWlERLmMDpSoulzB6X0q+JNmL+A4AAdjcE4FATgYgRmCHALiAA2eAu035zF3p-DJ39WgdH-oAnReMsydKVAMjCH5ElAxBQZcG0JIUIKibUJBat0GoOQegpx7k8GnXpGbYhwCl5hPIeUB2-jnbZTdhRauzDMCsP9uwuQXCHD0MoovIlRUe7CNEc4iJrMYBSMwS5GR6d4ztSGUjPOfU1H5ixpo0sY0qWMP0VfIxJirHAHMeUSxL4u6QpZpHGAh5UVwF2pise5RXFGvRDElAXj2VnSFPUE1R5soiJQJsYJSp7XqDjoi95KA4lRPbpC2ozqMX-gQIBeFCLngjHGXMGZ9RSnlLrpnLOp8lGjILmMQpxSk1zNTUTE5ZNLDT1sh62ISAEhgFLX2CAHqABSEBxT+piOstURQM36vjo0JozIZI9GyUA+Ci8szYAQMAUtUA4AQFslANYuawVQAsSG8Y47J2UBnXO0yuyE3FPlnCqWy7jGR2RSg7W6L0RmtwRa-BuKiFzxIUhbxdsSV+KdjQilLLqVyLpQHDhTKeEEu9QKXlBruUbQkfy+qgrGrCrkWKyonUYCSrRkXGVpcRraMVdNZVjYG53rugipEpiNVYpXUex6OrrHwpPaVAAVs2tAGIm3imvUG29OLJ5mxtRiXNL6fVvtqFQ8lxpA7YBQEQAAZqgdcQ63Y6j1DAPUhgsg0DdtkmARSQO+qRDa2KqoNSBjNPKMMSFNWs1uvBjOoEOoZtqLnDMYyxjoZLoWMupYTQmeU+GfGV8sWcf4ZPPwuteP8Z00J5k2BdYCmAIa+IDpNNymGCidddhHAqfi4liswwcie2ZSAaAKIbAFA4Nw5TyU4vDDU1TXNLhd6qsjvp56uCKNflqKxtAqTI2wKPb62NS67PDKzSMY5TEyZeEndTKttMoBTcQMGWAwBsDjsIMwz5XaoPs05tzXmvRjCZy1SG3rX5iMuhANwPAdqAsmw4BvY8fYLYCbukJ9eM9spBNI7HQLE9ah3fe4GBAU5nsUNqG9zelYEBrTMTdwjf37t7T7LxkHr2EeQ-9YG0QwbKMLbwN1qNfWY11FG2m8onURtXyAA)


## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
