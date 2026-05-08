# Screens

## MainActivity (Main Gallery)
- **Top:** Toolbar with the title "CatCurator".
- **Center:** SwipeRefreshLayout containing a RecyclerView.
- **RecyclerView:** 2-column or 3-column grid showing cat images.
- **Bottom/Floating:** A Refresh Button (if not using swipe-to-refresh exclusively).

## CatDetailActivity (Details Screen)
- **Top:** Large ImageView showing the selected cat.
- **Middle:** TextViews for Breed Name, Origin, and Temperament.
- **Content:** ScrollView containing the full description.
- **Action:** A "Favorite" toggle button (Heart icon).