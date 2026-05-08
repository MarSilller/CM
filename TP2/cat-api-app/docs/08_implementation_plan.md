# Implementation Plan

## Phase 1: Foundation (The Skeleton)
- **Step 1:** Initialize Android project (Kotlin, XML, ViewBinding).
- **Step 2:** Add dependencies (Retrofit, OkHttp, Glide/Coil, Room for caching, Lifecycle).
- **Step 3:** Implement `CatImage` and `Breed` data classes.
- **Step 4:** Create the `ApiService` interface and `RetrofitInstance`.

## Phase 2: UI & Data Flow (The Heart)
- **Step 5:** Design `activity_main.xml` (RecyclerView + SwipeRefreshLayout).
- **Step 6:** Create `CatAdapter` and `item_cat.xml` for the grid items.
- **Step 7:** Build `CatRepository` and `MainViewModel` to fetch data.
- **Step 8:** Connect `MainViewModel` to `MainActivity` to populate the list.

## Phase 3: Advanced Logic & Offline Support (The Brain)
- **Step 9: Database Setup.** Define Room Entity for `CatImage` and a DAO that supports inserting batches and clearing old data.
- **Step 10: FIFO Favorites.** Implement a specific DAO method or Repository logic that checks the count of favorites; if > 5, delete the oldest entry before adding a new one.
- **Step 11: Sliding Window Cache.** Implement Repository logic to maintain exactly 50 items. Ensure the UI triggers "pre-loading" so the cache always has 10 items ahead/behind the current scroll position.
- **Step 12: Offline Access.** Update the Repository to return the Room cache when `NetworkUtils` detects no internet connection.
- **Step 13: Image Details Screen.** Create `CatDetailActivity` with XML layout to show breed temperament, origin, and the "Favorite" toggle.