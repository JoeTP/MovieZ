# MovieZ

A modular, scalable Android app that showcases movies from The Movie Database (TMDB). The project applies Clean Architecture with an MVI (Model–View–Intent) presentation pattern, ensuring testability, separation of concerns, and ease of scaling new features.

## Architecture

- Clean Architecture with layers: `core/`, `domain/`, `data/`, and feature modules.
- MVI pattern in the presentation layer for predictable state management and unidirectional data flow.
- Fully modularized to ensure scalability and fast builds:
  - `app/` – Application container and navigation host.
  - `core/` – Cross-cutting concerns (networking, database, common UI/utilities, DI setup, configuration like API keys, etc.).
  - `domain/` – Use cases and business logic (pure Kotlin, framework-agnostic).
  - `data/` – Repositories, data sources (remote/local), DTOs, mappers.
  - `feature_movies_list/` – Movies list feature (screens, view models, MVI states/intents).
  - `feature_search/` – Search feature.
  - `feature_movie_details/` – Movie details feature.

## Features

- Movies List: Browse popular/trending movies.
- Search: Search for movies by title or keywords.
- Movie Details: Detailed page for a selected movie, including metadata and artwork.

## Tech Stack and Dependencies

Core libraries and tools used across modules (with versions managed in `gradle/libs.versions.toml`):

- Android
  - Android Gradle Plugin
  - AndroidX Core KTX: `androidx.core:core-ktx`
  - Activity Compose: `androidx.activity:activity-compose`
  - Lifecycle Runtime KTX: `androidx.lifecycle:lifecycle-runtime-ktx`
  - Navigation Compose: `androidx.navigation:navigation-compose`
  - Hilt Navigation Compose: `androidx.hilt:hilt-navigation-compose`
  - Jetpack Compose BOM and artifacts: `ui`, `ui-graphics`, `ui-tooling`, `ui-tooling-preview`, `material3`, `ui-test-junit4`, `ui-test-manifest`

- Dependency Injection
  - Hilt: `com.google.dagger:hilt-android`
  - Hilt Compiler (KSP): `com.google.dagger:hilt-android-compiler`

- Networking
  - Retrofit: `com.squareup.retrofit2:retrofit`
  - Gson Converter: `com.squareup.retrofit2:converter-gson`
  - OkHttp: `com.squareup.okhttp3:okhttp`
  - OkHttp Logging Interceptor: `com.squareup.okhttp3:logging-interceptor`

- Local Storage
  - Room: `androidx.room:room-runtime`, `androidx.room:room-ktx`, Compiler via KSP: `androidx.room:room-compiler`

- Images
  - Coil 3: `io.coil-kt.coil3:coil-compose`, `io.coil-kt.coil3:coil-network-okhttp`, `io.coil-kt.coil3:coil-svg`
  - (Project also includes compatibility artifact `io.coil-kt:coil-compose`)

- Serialization & Concurrency
  - Kotlinx Serialization JSON: `org.jetbrains.kotlinx:kotlinx-serialization-json`
  - Kotlin Coroutines: `org.jetbrains.kotlinx:kotlinx-coroutines-core`

- Testing
  - JUnit 4: `junit:junit`
  - AndroidX JUnit: `androidx.test.ext:junit`
  - Espresso: `androidx.test.espresso:espresso-core`
  - MockK: `io.mockk:mockk-android`, `io.mockk:mockk-agent`
  - Robolectric: `org.robolectric:robolectric`
  - AndroidX Core Testing: `androidx.arch.core:core-testing`

- Build/Plugins
  - Kotlin Android, Kotlin Compose, Kotlin Serialization
  - Android Application/Library
  - KSP (Kotlin Symbol Processing)
  - Hilt Android plugin

## Requirements

- Android Studio Ladybug or newer.
- JDK 17.
- Android SDK with compileSdk = 36, minSdk = 24.

## Getting Started (Installation & Setup)

1. Create a TMDB developer account and generate your API credentials:
   - Sign up at: https://developer.themoviedb.org/
   - Create an API key and a v4 Access Token (Bearer).

2. Add your credentials to your local.properties at the project root (create the file if it doesn't exist):

   ```properties
   API_KEY = "YOUR_KEY_HERE"
   ACCESS_TOKEN = "YOUR_TOKEN_HERE"
   ```

3. Sync Gradle in Android Studio.

4. Clean and build the project:
   - Android Studio: Build > Clean Project, then Build > Rebuild Project
   - Or via terminal:
     - Windows: `gradlew.bat clean assembleDebug`
     - macOS/Linux: `./gradlew clean assembleDebug`

5. Run the app on an emulator or a physical device running Android 7.0 (API 24) or above.

## Module Overview

- `app/` – Wires features together, hosts navigation, app-level theming, and entry point.
- `core/` – Provides DI setup (Hilt), Retrofit/OkHttp clients, Room database, image loading setup (Coil), common UI/components, and BuildConfig fields for TMDB credentials.
- `domain/` – Business rules and use-cases (e.g., `GetMovieDetailsUseCase`), `javax.inject` for interfaces, coroutine-based execution.
- `data/` – Repository implementations, remote (Retrofit) and local (Room), DTOs <-> domain mappers.
- `feature_movies_list/` – UI and MVI state for listing and paging movies.
- `feature_search/` – UI and MVI state for searching movies.
- `feature_movie_details/` – UI and MVI state for displaying details of a movie.

## Notes on MVI

- Unidirectional data flow: View emits Intents, ViewModel reduces state based on Result/Actions, View renders state.
- Clear separation of state, events, and side-effects leads to predictable, testable UIs.

## Security


- API keys and tokens are loaded from `local.properties` at build time and are not included in the source.
- Treat your TMDB credentials as secrets. Rotate them if you suspect exposure.

## Screen Shots "Supporting dark/light Theme"
- Using Material Theme and Color rules to implement Theming correctly
<div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px;">
  <img src="https://github.com/user-attachments/assets/e8926905-d957-4ba4-8012-8cb1b522622b" width="200px">
  <img src="https://github.com/user-attachments/assets/f2924424-1d63-40c6-80e2-245c8bac92fa" width="200px">
  <img src="https://github.com/user-attachments/assets/fa76bd97-cbc5-4d43-b045-96da6aab793f" width="200px">
  <img src="https://github.com/user-attachments/assets/e6bcec11-726b-4585-a36f-ecb2819a78a1" width="200px">
  <img src="https://github.com/user-attachments/assets/83880847-e0bc-4f71-9771-b620c1d00d47" width="200px">
  <img src="https://github.com/user-attachments/assets/920a243c-182b-45a9-aa48-31fc9e35f0aa" width="200px">
  <img src="https://github.com/user-attachments/assets/f8b69668-e45f-459e-a669-21ec80329bc5" width="200px">
</div>
