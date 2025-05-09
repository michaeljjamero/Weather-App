# Weather App 🌤️

A lightweight desktop weather application built using **JavaFX** and the **National Weather Service (NWS)** public API. This app displays current weather conditions and a 3-day forecast for the Chicago area, with an emphasis on clean UI and scalable architecture using software design patterns.

---

## Features

- **Today's Weather View**
  - Displays current temperature, short forecast, and weather icon with animation
  - Shows daily high and low temperatures
- **3-Day Forecast View**
  - Includes daytime and nighttime temperatures
  - Displays precipitation probability and wind speed/direction
- Smooth scene transitions and user-friendly interface
- Efficient API handling with caching to reduce redundant network calls

---

## Design Patterns Used

- **Proxy Pattern**
  - `MyWeatherAPI.java` acts as a cached wrapper around the real `WeatherAPI`
- **Template Method Pattern**
  - `WeatherSceneBuilder.java` defines a base layout template extended by scene-specific builders

---

## Technologies

- Java 17
- JavaFX
- Maven
- NWS API (https://api.weather.gov)

---

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/michaeljjamero/Weather-App.git
   cd Weather-App

2. Run the app using Maven:
   mvn clean compile exec:java

3. Ensure you are on the main branch:
   git checkout main
