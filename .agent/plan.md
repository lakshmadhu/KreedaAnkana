# Project Plan

Build an Android app in Kotlin named "Kreeda-Ankana" (Sports Ground & Match Organizer).

Purpose:
A village sports ground booking and match organization app for Volleyball and Cricket.

Features:

1. Ground Calendar Screen
- Display available and booked time slots in a grid calendar.
- Show team name and booked timing.

2. Book Slot Screen
- Allow users to select sport type (Cricket/Volleyball).
- Enter team name.
- Select date and time slot.
- Prevent double booking for the same slot.

3. Challenge Board Screen
- Teams can post match challenges.
- Other teams can reply to accept challenges.
- Use Firebase Realtime Database for storing challenges and replies.

4. Score Wall Screen
- Display latest match scores and results.

Database:
- Use Room Database for storing team profiles and match history.

UI Requirements:
- Bold sporty theme.
- Bottom navigation for Calendar, Booking, Challenge Board, Score Wall.
- Material Design UI.

Technical Requirements:
- MVVM architecture.
- Jetpack Compose for UI.
- Firebase integration (Realtime Database).
- Room Database.
- Form validation.
- Full Edge-to-Edge Display.
- Material Design 3 (M3).
- Vibrant, energetic color scheme.
- Adaptive app icon.

## Project Brief

# Project Brief: Kreeda-Ankana

Kreeda-Ankana is a vibrant, community-focused Android application designed to streamline sports ground bookings
 and match organization in rural and village settings. Focused on Volleyball and Cricket, the app bridges the gap between local teams by providing
 a digital platform for scheduling, challenging, and scorekeeping.

### Features
*   **Ground Calendar & Booking:** An
 interactive grid calendar displaying real-time availability for sports grounds. Users can book specific time slots for Cricket or Volleyball, with
 built-in validation to prevent double bookings.
*   **Real-time Challenge Board:** A dynamic hub where teams can post
 match challenges and accept invitations from rivals, powered by real-time synchronization.
*   **Live Score Wall:** A centralized dashboard to
 record and display the latest match results, keeping the community updated on local sports performance.
*   **Team & History
 Management:** Local storage of team profiles and a comprehensive history of past matches and bookings for easy reference.

### High-Level Technical Stack

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose with Material Design 3 (M
3)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Local Database:** Room (
using **KSP** for efficient code generation)
*   **Real-time Database:** Firebase Realtime Database
*   
**Concurrency:** Kotlin Coroutines & Flow
*   **Asynchronous Loading:** Coil for image rendering
*   **
Core Libraries:** Core KTX, Lifecycle (Runtime/ViewModel/Compose), and Navigation Compose

## Implementation Steps

### Task_1_Setup_Data_and_Navigation: Setup the project architecture including Firebase integration, Room database configuration, and Navigation shell with Material 3 theme.
- **Status:** COMPLETED
- **Updates:** 1. Project Architecture: Established an MVVM-ready structure with dedicated packages for data (Room) and UI (Navigation/Theme/Components).
2. Room Database: Implemented entities for Team, Booking, and Match. Created DAOs (TeamDao, BookingDao, MatchDao). Initialized AppDatabase in KreedaAnkanaApplication.
3. Firebase Integration: Added Firebase dependencies. Note: google-services.json still needs to be provided for actual connectivity.
4. Navigation Shell: Implemented NavHost with placeholder screens and a BottomNavigationBar using Material 3.
5. Material 3 Theme: Designed a vibrant, sporty theme with Dynamic Color support and full Edge-to-Edge display.
6. App Icon: Generated an adaptive app icon.
7. Build Validation: App builds successfully.
- **Acceptance Criteria:**
  - Firebase Realtime Database initialized
  - Room entities (Booking, Team, Match) and DAO created
  - Navigation graph with BottomBar implemented
  - App builds and launches with a basic shell
- **Duration:** N/A

### Task_2_Ground_Booking_System: Implement the Ground Calendar and Book Slot screens using Room for local persistence.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Grid-based calendar displays bookings
  - Booking form with sport type, team name, and date/time
  - Validation logic to prevent double bookings
  - Bookings persist in Room and update UI
- **StartTime:** 2026-04-30 23:02:38 IST

### Task_3_Realtime_Challenge_Board: Implement the Challenge Board using Firebase Realtime Database for cross-team interaction.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Users can post match challenges to Firebase
  - Challenges are displayed in real-time to other users
  - Acceptance/Reply mechanism functional
  - Real-time updates confirmed

### Task_4_Score_Wall_and_Refinement: Implement the Score Wall for match results and apply final UI/UX refinements.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Score Wall displays match results from Room/Firebase
  - Full Edge-to-Edge display implemented
  - Adaptive app icon matching the sports theme created
  - Vibrant Material 3 color scheme applied

### Task_5_Final_Verification: Perform a comprehensive run and verify all application features and stability.
- **Status:** PENDING
- **Acceptance Criteria:**
  - App does not crash during navigation or data operations
  - All features (Booking, Challenges, Scores) functional
  - UI aligns with Material 3 and sports theme
  - Final build passes successfully

