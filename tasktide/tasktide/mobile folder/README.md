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
   - `ui/screens/LoginScreen.kt`
   - `ui/screens/RegisterScreen.kt`
   - `ui/screens/DashboardScreen.kt`

## Backend API used

- `POST /users/register`
- `POST /users/login`
- `GET /users/all` (used to resolve user_id after login)
- `GET /tasks/user/{userId}`
- `POST /tasks/add`
- `DELETE /tasks/delete/{taskId}`

## Important setup

1. Start your backend first.
2. If running Android emulator, base URL is already set to:
   - `http://10.0.2.2:8080/`
3. If running on a real phone, update base URL in:
   - `app/src/main/java/edu/cit/asia/tasktide/mobile/api/ApiClient.kt`

## Open in Android Studio

1. Open Android Studio.
2. Select "Open".
3. Choose this folder: `tasktide/mobile folder`.
4. Let Gradle sync.
5. Run app on emulator/device.

## Notes

- Backend currently returns only JWT on login, so this app resolves the user's ID by calling `/users/all` and matching by email.
- Local tasks are stored in Room and refreshed from API when available.
- A cleaner backend approach is returning `{ token, user_id }` from `/users/login`.
