# Restaurant App - Android

A simple Android application for managing a restaurant. It allows users to register, log in, browse the menu, and add new items to the menu. The app is built using modern Android development practices and utilizes Firebase for backend services.

## ✨ Features

*   **User Authentication:**
    *   Secure user registration with email and password.
    *   User login system.
*   **Menu Management:**
    *   View a list of available menu items.
    *   Add new items to the menu.
*   **User-Friendly Interface:**
    *   Clean and intuitive UI.
    *   Engaging animations using Lottie.

## 📸 Screenshots

*(Yahan aap apne app ke screenshots daal sakte hain)*

| Login Screen | Register Screen | Menu Screen | Add Item Screen |
| :---: | :---: |:---: |:---: |
| *(Image of Login Screen)* | *(Image of Register Screen)* | *(Image of Menu Screen)* | *(Image of Add Item Screen)* |

## 🛠️ Technologies & Libraries Used

*   **Language:** Kotlin
*   **UI:** XML Layouts
*   **Backend & Database:**
    *   Firebase Authentication for user management.
    *   Firebase Realtime Database for storing menu data.
*   **UI Components:**
    *   Material Components for Android
    *   AndroidX Libraries (AppCompat, ConstraintLayout, etc.)
*   **Animations:** Lottie for Android for rich animations.
*   **Build Tool:** Gradle

## 🚀 Getting Started

In instructions ko follow karke aap project ko apne local machine par chala sakte hain.

### Prerequisites

*   Android Studio (latest version recommended)
*   Ek Google account Firebase ke liye.

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/your-username/RestaurantApp.git
    ```
2.  **Android Studio me open karein:**
    *   Android Studio open karein.
    *   `File` > `Open` par click karein aur clone kiye hue project directory ko select karein.
    *   Android Studio ko project build aur sync karne dein.

3.  **Firebase Setup:**
    *   [Firebase Console](https://console.firebase.google.com/) par jaayein.
    *   Ek naya project banayein.
    *   Apne Firebase project me ek Android app add karein `com.example.restaurantapp` package name ke saath (ya aapke app ka actual package name).
    *   Setup instructions follow karke `google-services.json` file download karein.
    *   `google-services.json` file ko apne Android Studio project ke `app/` directory me rakhein.
    *   Firebase Console me, **Authentication** section me jaayein aur **Email/Password** sign-in method enable karein.
    *   **Realtime Database** section me jaayein, ek database banayein, aur security rules set karein. Development ke liye, aap unhe public set kar sakte hain:
        ```json
        {
          "rules": {
            ".read": "true",
            ".write": "true"
          }
        }
        ```
        **Note:** Ye rules insecure hain aur production environment ke liye aage aacche se configure karne chahiye.

4.  **Run the app:**
    *   Ek target device (emulator ya physical device) select karein.
    *   Android Studio me `Run` button par click karein.

## Project Structure
```
RestaurantApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/restaurantapp/  # Kotlin source files
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── RegisterActivity.kt
│   │   │   │   ├── MenuActivity.kt
│   │   │   │   └── AddItemActivity.kt
│   │   │   ├── res/                              # Resources
│   │   │   │   ├── layout/                       # XML Layout files
│   │   │   │   ├── drawable/                     # Images and drawables
│   │   │   │   ├── raw/                          # Lottie animation files
│   │   │   │   └── values/                       # colors, strings, styles
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle                          # App-level build script
└── build.gradle                                  # Project-level build script
```

