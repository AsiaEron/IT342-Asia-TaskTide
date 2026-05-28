# TaskTide Mobile (Kotlin + XML)

This folder contains a standalone Android app for your Spring Boot backend.

## Location

- `tasktide/mobile folder`

## Technology stack

- Kotlin
- Jetpack Compose (single-activity app)
- Retrofit + OkHttp
- Room (local task cache with offline fallback)

## UI architecture

- `LoginActivity` is the single activity host.
- `TaskTideViewModel` handles state and actions.
- Compose screens are split into:
   - `features/auth/LoginScreen.kt`
   - `features/auth/RegisterScreen.kt`
   - `features/dashboard/DashboardScreen.kt`

## Backend API used

- `POST /users/register`
- `POST /users/login`
- `GET /api/tasks/user/{userId}`
- `POST /api/tasks`
- `DELETE /api/tasks/{taskId}`

## Important setup

1. Start your backend first.
2. If running Android emulator, base URL is already set to:
   - `http://10.0.2.2:8080/`
3. If running on a real phone, update base URL in:
   - `app/src/main/java/edu/cit/asia/tasktide/mobile/shared/network/ApiClient.kt`

## Open in Android Studio

1. Open Android Studio.
2. Select "Open".
3. Choose this folder: `tasktide/mobile folder`.
4. Let Gradle sync.
5. Run app on emulator/device.

## Notes

- The mobile app expects the backend login response to include `token` and `userId`, which matches the current backend `AuthResponseDTO`.
- The app stores token and user ID locally, then uses them to load tasks and add/delete tasks.
- Local tasks are stored in Room and refreshed from the backend when available.
- The mobile app does not currently use `/users/all` for normal login flow.
- If you change the backend host, update `BASE_URL` in `app/src/main/java/edu/cit/asia/tasktide/mobile/shared/network/ApiClient.kt`.
