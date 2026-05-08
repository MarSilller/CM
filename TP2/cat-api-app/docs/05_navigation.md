# Navigation

The app uses standard Intent-based navigation:

1. **MainActivity** → **CatDetailActivity**
    - Trigger: User clicks on a specific image in the grid.
    - Data Passed: The `CatImage` object or its ID via Intent extras.

2. **CatDetailActivity** → **MainActivity**
    - Trigger: User presses the Up/Back button.