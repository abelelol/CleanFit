# CleanFit 🧥✨

**CleanFit** is my take on a digital closet companion. It's a native Android app built with **Jetpack Compose** that helps you organize your wardrobe by snapping photos. The app uses on-device machine learning (MobileNet_V3 Model) to identify your clothes, figure out their colors, and eventually suggest some killer outfits.

Main purpose of me building out this Android App is that I want to actually do Android dev full time. I personally have years of me doing full stack dev work and I thought this project would be a cool transition space for me to learn the craft and use it as stepping stone for me to apply to some android dev roles.

## 🚀 What It Does

Basically I go ahead and use CameraX to connect your Androids Camera and I use the MLKit Api to help pass images to a analyzer to identify an images. Quick note though the base object detection capabablities of MLKit is pretty bad. So I went ahead and found a tensorflow model (MobileNet_V3 Model) to get better responses and just used mlkit as a wrapper to help pass the images to the model. This is where I also decided to use Pallete API, a Jetpack libary to help me identify the main and tertiary colors of your clothing item (object detection models can't see color since they are priotizing object detection; all images passed to them are black and white). Then I just use normal color theory to recommend you clothing items that would match your current attire. If you want to learn more of course check out the code here or just reach out to me. 

## 🛠️ The Cool Tech Under the Hood

- **Language:** Kotlin, TypeScript (for Supabase calls, no row level security here).
- **UI:** **Jetpack Compose** + Material 3.
- **Navigation:** The brand new **Navigation 3** (`androidx.navigation3`).
- **Identification:** **ML Kit** for object detection, **MobileNet_V3 Model** for the model,  **CameraX** for the camera feed and **Pallete** for color detection.
- **DI:** **Hilt** for dependency injection.
- **Async:** Coroutines & Flow.
- **Architecture:** MVVM.

## 🎥 Video Demo

https://github.com/user-attachments/assets/7caeafc0-8482-4a77-9df6-6dc34facaae9


## 🏃 Getting Started

1. **Clone the repo:**
   `git clone https://github.com/yourusername/cleanfit.git`

2. **Open in Android Studio** (Ladybug or newer recommended).

3. **Sync Gradle** and hit **Run** on your device/emulator.

## 🔮 What's Coming Next
- General clean up
- Resolving minor issues and adding features (pulling in to many clothing items even after hard coding limit in query, try to optimize current ml models some more, connect user info with data stored in supabase, etc)
- Add some Unit Tests
- Run another model to help identify logos/brands for clothing items.
- Release this app on the PlayStore 🙂.

## 📄 License

MIT License. Feel free to use this code to learn!
