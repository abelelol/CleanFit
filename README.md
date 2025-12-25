CleanFit 🧥✨

CleanFit is my take on a digital closet companion. It's a native Android app built with Jetpack Compose that helps you organize your wardrobe by snapping photos. The app uses on-device machine learning to identify your clothes, figure out their colors, and eventually suggest some killer outfits.

 I built CleanFit to get my hands dirty with modern Android development standards while building something that's actually unique and fun to use.

🚀 What It Does

Snap & Scan: Uses CameraX to capture your fits.

AI Magic: Runs ML Kit (and soon custom TensorFlow Lite models) to figure out if that's a shirt, pants, or shoes, and even spots brand logos.

Style Matcher: Analyzes colors to help you pair items together (no more clashing outfits).

Smart Login: Uses the new Android Credential Manager for smooth Google Sign-Ins.

🛠️ The Cool Tech Under the Hood

I wanted to use the latest and greatest tools available for Android right now. No legacy code here.

Language: Kotlin (obviously).

UI: Jetpack Compose + Material 3.

Navigation: The brand new Navigation 3 (androidx.navigation3) — fully type-safe and state-driven.

AI/ML: ML Kit for object detection and CameraX for the camera feed.

DI: Hilt for dependency injection.

Async: Coroutines & Flow.

Architecture: It follows a solid Clean Architecture pattern (UI/Domain/Data), so the code is actually scalable and testable, not just a spaghetti mess.

🎯 Why CleanFit?

I wanted to move beyond beginner tutorials and build a real-world application that handles:

Real hardware interactions (Camera).

On-device Intelligence (ML models).

Modern Architecture (Clean Arch + MVVM) effectively.

It's a playground for learning the right way to build Android apps in 2025.

🏃 Getting Started

Clone the repo:

git clone [https://github.com/yourusername/cleanfit.git](https://github.com/yourusername/cleanfit.git)


Open in Android Studio (Ladybug or newer recommended).

Sync Gradle and hit Run on your device/emulator.

🔮 What's Coming Next

Training custom TFLite models to recognize specific streetwear logos.

Building out the local database to save your closet.

Adding a "Mix & Match" playground screen.

📄 License

MIT License. Feel free to use this code to learn!
