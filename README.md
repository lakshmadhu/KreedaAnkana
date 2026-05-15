Kreeda-Ankana (Sports)

Kreeda-Ankana is a "Ground & Match Organizer" designed to transform village sports grounds into organized community hubs. By digitizing the "notice board" of the village ground, it allows teams to book slots fairly, find opponents from neighboring villages, and build a vibrant sports culture.

Problem Statement
In many rural areas, sports grounds are often occupied by the same groups, leaving others without a chance to play. There is no existing system to "Book a Slot" or "Find an Opponent," making it difficult for small teams to organize matches with neighboring communities. Kreeda-Ankana solves this by providing a digital notice board for ground management and match coordination.

Key Features
Ground Calendar: A grid-based view to see current and upcoming bookings. 
Smart Booking: Real-time logic that prevents double-booking of time slots.
Challenge Board: Post and accept challenges (e.g., Volleyball/Cricket) with real-time replies.
Score Wall: A digital results board to celebrate the latest village match outcomes.
Team Management:Create and manage team profiles with match history.

Tech Stack & Architecture
Language: Kotlin (100%)
UI Framework: Jetpack Compose (Modern, bold, sporty UI)
Backend: Firebase Realtime Database (Live challenge synchronization)
Authentication: Firebase Auth (Secure login for team captains)
Local Storage:Room Persistence Library (Offline caching for profiles)
Architecture: MVVM (Model-View-ViewModel)
Navigation:Jetpack Navigation with Pager integration for swipe transitions.

Folder Structure
```text
app/src/main/java/com/example/kreedaankana/
├── ui/              # Composables, Themes, and Screen Layouts
├── viewmodel/       # UI Logic and State Management
├── repository/      # Data handling (Firebase & Room)
├── data/            # Room DB Entities and DAOs
└── navigation/      # Navigation graphs and screen definitions



Developed as part of the Android App Development using GenAI - Project Phase.
