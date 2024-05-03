# Sky 🌤️

**Sky** is a sleek weather app with elegant design and delightful animations, providing you with essential weather updates and forecasts in a cozy interface.

Supported by **The White Wings**🪽. Play Store [link](https://play.google.com/store/apps/dev?id=6456450686494659010)

<br>

## Navigate Your Journey of exploring Sky 🗺️
 1. [Why Should I Always Check the Sky?](#why-should-i-always-check-the-sky-)
 2. [Quick Start](#quick-start-)
 3. [Usage](#usage-)
 4. [Demonstration](#demonstration-)
 5. [Utilized Technologies](#utilized-technologies-)
 6. [Main Files](#main-files-)

<br>

## Why Should I Always Check the Sky? 💡
Imagine a world where checking the weather feels like a breath of fresh air rather than a chore. That's the experience Sky offers—a seamless blend of elegance and simplicity that turns mundane tasks into delightful moments.

 - **A Delightful Journey:** With Sky, every weather check becomes an adventure.<br>
Its soothing color palette, delightful animations, and charming 3D weather icons transport you to a world where even the forecast is a thing of beauty.

 - **Escape the Clutter:** Say goodbye to overwhelming weather info.<br>
Sky's thoughtful design ensures you're never bombarded with unnecessary information. Its clean interface prevents screen clutter, allowing you to focus on what truly matters—your plans for the day!

 - **Effortless Efficiency:** Sky isn't just about looks—it's about performance too.<br>
By prioritizing essential details and eliminating distractions, Sky keeps the app light and nimble. Say hello to smooth, responsive weather updates.

Join the journey with Sky and discover the joy of weather-checking done right.

<br>

## Quick Start 🚀
**Clone** the Repository and **Run** the app on your device or emulator.

After that, for **Setting Your Home City**:
 1. **Open Cities Screen**: Click on *"Set city"* in the dialog shown when you first open the app, or tap the *"+" button* in the middle of the tab bar at the bottom of the main screen.
 2. **Search for Your City**: Use the search field to find your desired city or location.
 3. **Add Your City**: Once found, a widget for your city will be automatically added.
 4. **Set as Home**: *Long press* on the city widget to reveal a pop-up menu. Choose *"Set as home"* to designate this city as your home location.
 5. **Navigate Back**: Return to the main screen to view the forecast for your home city.

<br>

## Usage 📱
On the Main Screen:
 - **Main Home City Information:** View the name of your home city, current temperature, weather condition description, and today's highest and lowest temperatures.
 - **Hourly and Daily Forecasts:** Access detailed forecasts for the upcoming hours and days with just a tap on *"Daily Forecast"* or *"Hourly Forecast"* in the Collapsed Bottom Sheet.

<br>

In the Expanded Bottom Sheet:
 - **Detailed Forecast:** Explore comprehensive weather information, including UV index, sunrise and sunset times, wind speed and direction, rainfall, feels like temperature, humidity, dew point, and visibility.

<br>

On the Cities Screen:
 - **Search and Add Cities:** Utilize the search field to discover and add new cities quickly.
 - **Manage Cities:** Long press on a city widget to designate it as your home city or remove it. Slide a city widget to the left to remove it effortlessly.

<br>

## Demonstration 📸
Click the image below to watch the full app demo on YouTube ⬇️

[![Full demo](https://github.com/TheMaestroCo/Sky/assets/75887565/bcdce771-8ded-464f-9fe7-fb72c2b63f94)](https://www.youtube.com/watch?v=knCZisHvEII)

<br>

## Utilized Technologies 🔧
 - **Programming Language:** Java

 - **Frameworks/Libraries:**
   - **Volley**: For making **REST API** calls.
   - **Motion Layout** and **ObjectAnimator**: For creating smooth animations.
   - **Glide**: For efficient image loading and caching.
   - **SharedPreferences**: For storing and retrieving user's cities list.

 - **Development Tools:** Postman, Figma, PlaygroundAI, Lottie Animations, Git, and Android Studio.

 - **APIs:**
   - OpenStreetMap's Nominatim API: Provides the Latitude and Longitude Coordinates of the provided city/location. API [link](https://nominatim.org/release-docs/develop/api/Overview/)
   - Open Meteo API: Offers Weather data based on the provided Coordinates. API [link](https://open-meteo.com/)

Special thanks to DesignCode for their exceptional work in crafting the intuitive and visually appealing design of Sky.

<br>

## Main Files 📁
 - [MainActivity.java](app/src/main/java/com/thewhitewings/sky/MainActivity.java)
 - [activity_main.xml](app/src/main/res/layout/activity_main.xml)
 - [CitiesActivity.java](app/src/main/java/com/thewhitewings/sky/CitiesActivity.java)
 - [activity_cities.xml](app/src/main/res/layout/activity_cities.xml)
 - [WeatherDataService.java](app/src/main/java/com/thewhitewings/sky/WeatherDataService.java)

<br></br>
Don't forget to always check The **Sky** ✨
