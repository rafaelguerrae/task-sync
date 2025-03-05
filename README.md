# TaskSync

TaskSync is a collaborative task management app for Android that allows users to create, share, and track tasks in real time. Built with modern Android tools such as Jetpack Compose, Room for offline caching, Firebase for authentication and data storage, and Hilt for dependency injection.

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)

## Features
- **User Authentication:** Secure login and registration using Firebase Authentication (Email/Password, Google Sign-In).
- **Task Management:** Create, update, delete, and view tasks with real-time updates via Firestore.
- **Offline Support:** Local caching using Room to provide a smooth experience even without internet connectivity.
- **Push Notifications:** Receive notifications for task updates and reminders via Firebase Cloud Messaging.
- **Clean Architecture:** Utilizes MVVM pattern and Hilt for dependency injection, ensuring maintainable and testable code.
- **Modern UI:** Built with Jetpack Compose for a responsive and intuitive user interface.

## Tech Stack
- **Programming Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Database:** Room (for offline caching), Firestore (for real-time data)
- **Authentication:** Firebase Authentication
- **Cloud Messaging:** Firebase Cloud Messaging
- **Dependency Injection:** Hilt
- **Testing:** JUnit