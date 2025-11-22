# Sea Battle (Battleship)

![Java](https://img.shields.io/badge/Java-11%2B-orange.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-blue.svg)
![Log4j2](https://img.shields.io/badge/Log4j-2.14%2B-green.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

**Sea Battle** is a comprehensive multiplayer implementation of the classic strategy game Battleship. Built using **Java** and **JavaFX**, this project utilizes a client-server architecture to manage game states, user sessions, and real-time interactions. It features a modern GUI, configurable game logic, and support for spectating matches.

## Description

This project separates game logic into a distinct **Server** and **Client**. The server handles matchmaking, board generation, and state validation, while the client provides a rich visual interface for players to log in, arrange their fleets, and battle opponents. The system is designed with extensibility in mind, utilizing `properties` files for dynamic configuration and Log4j2 for robust error tracking and event logging.

## Features

* **Client-Server Architecture:** Dedicated server handling multiple client connections via sockets.
* **Interactive GUI:** Responsive interface built with JavaFX, featuring animated transitions and drag-and-drop mechanics (implied by arrangement logic).
* **Ship Arrangement:** Timed phase for players to position their fleet (Frigates, Destroyers, Cruisers, Battleships) with server-side validation.
* **Spectator Mode:** Functionality allowing users to watch ongoing matches (inferred from project structure).
* **Configurable Gameplay:** customizable settings for turn timers, arrangement limits, and connection ports via external property files.
* **Leaderboard & Status:** Systems to track player performance and game status.

## Installation

### Prerequisites
* **Java Development Kit (JDK):** Version 11 or higher (required for JavaFX).
* **IDE:** IntelliJ IDEA (recommended) or Eclipse.

### Setup

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/nikelroid/sea-battle.git](https://github.com/nikelroid/sea-battle.git)
    ```
2.  **Import Project:** Open the project in your IDE. Ensure the project structure identifies both `Client` and `Server` modules.
3.  **Dependencies:** Ensure the `lib` folder libraries (Log4j2, cfg4j, etc.) are added to your project's classpath.

## Usage

To run the game, you must start the server first, followed by one or more clients.

### 1. Start the Server
Navigate to the `Server` module and run the main class:
* **Class:** `launch.Main`
* **Configuration:** Modify `Server/serverConfig.properties` to change the hosting port (Default: 4042).

### 2. Start the Client
Navigate to the `Client` module and run the application:
* **Class:** `com.java.lunch.Main`
* **Configuration:** Modify `Client/clientConfig.properties` to adjust connection settings and game rules (e.g., `arrange_time`, `play_time`).

```properties
# Example Client Configuration
address=127.0.0.1
port=4042
arrange_time=30
play_time=25
````

### 3\. Gameplay

  * **Login:** Enter your credentials on the starting screen.
  * **Arrangement:** You have a limited time (default 30s) to arrange your ships. Use the "Ready" button to confirm or "Re-arrange" to reset.
  * **Battle:** Take turns firing at the opponent's grid.

## Contributing

Contributions are welcome\! Please follow these steps:

1.  Fork the repository.
2.  Create a feature branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your changes.
4.  Push to the branch.
5.  Open a Pull Request.

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

**Nima Kelidari**
Project Link: [https://github.com/nikelroid/sea-battle](https://www.google.com/search?q=https://github.com/nikelroid/sea-battle)

