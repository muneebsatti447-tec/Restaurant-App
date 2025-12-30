📌 Overview

This is a simple Android application designed for managing a restaurant. The app allows users to register, log in, browse the restaurant menu, and add new menu items.
It is developed using modern Android development practices and uses Firebase for backend services.

✨ Features
🔐 User Authentication

Secure user registration using email and password

User login system powered by Firebase Authentication

🍽️ Menu Management

View a list of available menu items

Add new items to the menu

🎨 User-Friendly Interface

Clean and intuitive UI design

Engaging animations using Lottie

📸 Screenshots

(You can add screenshots of your application here)

Login Screen	Register Screen	Menu Screen	Add Item Screen
Image	Image	Image	Image
🛠️ Technologies & Libraries Used
🧑‍💻 Development

Language: Kotlin

UI: XML Layouts

☁️ Backend & Database

Firebase Authentication – User management

Firebase Realtime Database – Menu data storage

📦 Libraries & Tools

Material Components for Android

AndroidX Libraries (AppCompat, ConstraintLayout, etc.)

Lottie for Android – Animations

Gradle – Build system

🚀 Getting Started

Follow the steps below to run the project on your local machine.

✅ Prerequisites

Android Studio (latest version recommended)

A Google account for Firebase

⚙️ Installation & Setup
1️⃣ Clone the Repository
git clone https://github.com/your-username/RestaurantApp.git

2️⃣ Open the Project in Android Studio

Open Android Studio

Click File > Open

Select the cloned project directory

Allow Android Studio to build and sync the project

3️⃣ Firebase Setup

Go to Firebase Console

Create a new Firebase project

Add an Android app with the package name
com.example.restaurantapp (or your actual package name)

Download the google-services.json file

Place it inside the app/ directory of your project

Enable Authentication

Go to Authentication → Sign-in method

Enable Email/Password authentication

Configure Realtime Database

Go to Realtime Database

Create a database

Set the following rules for development:

{
  "rules": {
    ".read": "true",
    ".write": "true"
  }
}


⚠️ Note: These rules are insecure and should be properly configured for production use.

4️⃣ Run the Application

Select a target device (emulator or physical device)

Click the Run button in Android Studio

📂 Project Structure
RestaurantApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/restaurantapp/   # Kotlin source files
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── RegisterActivity.kt
│   │   │   │   ├── MenuActivity.kt
│   │   │   │   └── AddItemActivity.kt
│   │   │   ├── res/                               # Resources
│   │   │   │   ├── layout/                        # XML layout files
│   │   │   │   ├── drawable/                      # Images and drawables
│   │   │   │   ├── raw/                           # Lottie animation files
│   │   │   │   └── values/                        # Colors, strings, styles
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle                           # App-level Gradle file
└── build.gradle                                   # Project-level Gradle file
