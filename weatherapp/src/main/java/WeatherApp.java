/*-----------------------------------
Weather App
Michael Jamero
----------------------------------*/
import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;
import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import java.util.Date;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.ArrayList;

public class WeatherApp extends Application {
	private Scene todayScene;
	private Scene forecastScene;
	private Label todayShortForecast;
	private Button toForecastBtn;
	private Button toTodayBtn;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Weather App");

		ArrayList<Period> forecast = WeatherAPI.getForecast("LOT", 77, 70);
		if (forecast == null) {
			throw new RuntimeException("Forecast did not load");
		}


		//-----------Scene 1: Today's Weather----------//
		String location = "Chicago, IL";
		Date now = new Date();

		Period currentPeriod = null;
		int highTemp = Integer.MIN_VALUE;
		int lowTemp = Integer.MAX_VALUE;

		// Find current period and high/low temps
		for (Period p : forecast) {
			if (p.startTime != null && p.endTime != null) {
				if (now.after(p.startTime) && now.before(p.endTime)) {
					currentPeriod = p;
				}

				// Track highs and lows for today only
				if (p.startTime.getDay() == now.getDay()) {
					if (p.isDaytime) {
						highTemp = Math.max(highTemp, p.temperature);
					} else {
						lowTemp = Math.min(lowTemp, p.temperature);
					}
				}
			}
		}

		if (currentPeriod == null) currentPeriod = forecast.get(0);

		// Current Weather Icon
		ImageView weatherIcon = null;
		try {
			String iconUrl = currentPeriod.icon;
			Image icon = new Image(iconUrl, 100, 100, true, true);
			weatherIcon = new ImageView(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Weather Icon Animation
		TranslateTransition bounce = new TranslateTransition(Duration.millis(1000), weatherIcon);
		bounce.setByY(-15);            // Bounce height
		bounce.setAutoReverse(true);   // Come back down
		bounce.setCycleCount(TranslateTransition.INDEFINITE);
		bounce.play();

		// Title
		Label title = new Label("Today's Weather");
		title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

		// Labels
		Label locationLabel = new Label("Location: " + location);
		Label forecastLabel = new Label("Forecast: " + currentPeriod.shortForecast);
		Label highTempLabel = new Label("High: " + (highTemp != Integer.MIN_VALUE ? highTemp + "°F" : "N/A"));
		Label lowTempLabel = new Label("Low: " + (lowTemp != Integer.MAX_VALUE ? lowTemp + "°F" : "N/A"));

		// Button to view 3-Day Forecast
		Button toForecastBtn = new Button("View 3-Day Forecast");

		// Wrap labels in boxes
		VBox locationBox = new VBox(locationLabel);
		locationBox.setStyle("-fx-padding: 10; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: white;");

		VBox forecastBox = new VBox(forecastLabel);
		forecastBox.setStyle("-fx-padding: 10; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: white;");

		VBox highBox = new VBox(highTempLabel);
		highBox.setStyle("-fx-padding: 10; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: white;");

		VBox lowBox = new VBox(lowTempLabel);
		lowBox.setStyle("-fx-padding: 10; -fx-font-weight: bold; -fx-border-color: black; -fx-background-color: white;");

		// Layout
		VBox todayLayout = new VBox(20,
				title,
				weatherIcon,
				locationBox,
				forecastBox,
				highBox,
				lowBox,
				toForecastBtn
		);
		todayLayout.setStyle("-fx-alignment: center; -fx-padding: 30; -fx-background-color: lightblue;");
		todayScene = new Scene(todayLayout, 500, 600);

		//--------Scene 2: 3-Day Forecast--------//
		VBox forecastLayout = new VBox(15);
		forecastLayout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: lightgray;");

		// Title
		Label forecastTitle = new Label("3-Day Forecast");
		forecastTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		forecastLayout.getChildren().add(forecastTitle);

		// Retrieves data for each day in the 3-day forecast
		for (int i = 0; i < 3; i++) {
			Period day = forecast.get(i * 2);
			Period night = forecast.get(i * 2 + 1);

			String rainDisplay = (day.probabilityOfPrecipitation.value >= 0)
					? day.probabilityOfPrecipitation.value + "%"
					: "N/A";

			// Labels
			VBox card = new VBox(5);
			card.setStyle("-fx-border-color: black; -fx-font-weight: bold; -fx-background-color: white; -fx-padding: 10; -fx-min-width: 300;");
			card.getChildren().addAll(
					new Label("Day " + (i + 1) + ": " + day.temperature + "°F"),
					new Label("Night: " + night.temperature + "°F"),
					new Label("Rain Probability: " + rainDisplay),
					new Label("Wind: " + day.windSpeed + " " + day.windDirection)
			);

			forecastLayout.getChildren().add(card);
		}

		// Button to switch to the Today's Weathe from 3-Day Forecast
		Button toTodayBtn = new Button("Back to Today's Weather");
		forecastLayout.getChildren().add(toTodayBtn);
		forecastScene = new Scene(forecastLayout, 500, 600); // match size to todayScene

		// Switch to forecast scene
		toForecastBtn.setOnAction(e -> {
			boolean wasMaximized = primaryStage.isMaximized();
			primaryStage.setScene(forecastScene);
			if (wasMaximized) {
				Platform.runLater(() -> primaryStage.setMaximized(true));
			}
		});

		// Switch back to today's scene
		toTodayBtn.setOnAction(e -> {
			boolean wasMaximized = primaryStage.isMaximized();
			primaryStage.setScene(todayScene);
			if (wasMaximized) {
				Platform.runLater(() -> primaryStage.setMaximized(true));
			}
		});

		// Show app
		primaryStage.setScene(todayScene);
		primaryStage.show();
	}

}

