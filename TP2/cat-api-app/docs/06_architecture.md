# Architecture: MVVM

The application follows the Model-View-ViewModel pattern.

### Components:
- **View (XML + Activity):** Observes LiveData/StateFlow and displays it.
- **ViewModel:** Handles UI logic, interacts with the Repository, and survives configuration changes.
- **Repository:** Decides if data comes from the network (`CatApiService`) or local storage (`Room Database`).
- **Local Data:** Room DB for caching 50 items and the 5 favorites.
- **Remote Data:** Retrofit service for TheCatAPI.