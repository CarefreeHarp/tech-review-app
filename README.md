# Tech Review App

Android technology-review application created for the Mobile Computing course at Pontificia Universidad Javeriana.

The application is envisioned as a small social network where people can discover technology products, rate them, and share reviews with the community.

## Design

### Interface prototype

The Devicers interface prototype is available on <a href="https://www.figma.com/design/l7mETjAKWkyAFwtx1VWmIX/Untitled?node-id=1-484&amp;t=p0lttPj7kJNiSUG2-1" target="_blank" rel="noopener noreferrer">Figma</a>.

| Access                                                                                | Register                                                                                      | Home                                                                              | Search products                                                                                       |
| ------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| <img src="ReadMeAssets/AppAccessScreen.png" alt="Devicers access screen" width="220"> | <img src="ReadMeAssets/AppRegisterScreen.png" alt="Devicers registration screen" width="220"> | <img src="ReadMeAssets/AppHomeScreen.png" alt="Devicers home screen" width="220"> | <img src="ReadMeAssets/AppSearchProductsScreen.png" alt="Devicers product search screen" width="220"> |

| Create review                                                                                      | Activity                                                                                  | Own profile                                                                                    | Search profiles                                                                                      |
| -------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| <img src="ReadMeAssets/AppCreateReviewScreen.png" alt="Devicers create review screen" width="220"> | <img src="ReadMeAssets/AppActivityScreen.png" alt="Devicers activity screen" width="220"> | <img src="ReadMeAssets/AppOwnProfileScreen.png" alt="Devicers own profile screen" width="220"> | <img src="ReadMeAssets/AppSearchProfileScreen.png" alt="Devicers profile search screen" width="220"> |

| Found products                                                                                       | Profile results                                                                                                     | Saved reviews                                                                                             | Request product                                                                                        |
| ---------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------ |
| <img src="ReadMeAssets/AppFoundProductsScreen.png" alt="Devicers found products screen" width="220"> | <img src="ReadMeAssets/AppProfileSearchResultsScreen.png" alt="Devicers profile search results screen" width="220"> | <img src="ReadMeAssets/AppProfileSavedReviewsScreen.png" alt="Devicers saved reviews screen" width="220"> | <img src="ReadMeAssets/AppRequestProductScreen.png" alt="Devicers request product screen" width="220"> |

| Product                                                                                        | Rate product                                                                                     | Review                                                                                       | Public profile                                                                                       |
| ---------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| <img src="ReadMeAssets/AppProductScreen.png" alt="Devicers product detail screen" width="220"> | <img src="ReadMeAssets/AppRateProductScreen.png" alt="Devicers rate product screen" width="220"> | <img src="ReadMeAssets/AppReviewScreen.png" alt="Devicers review detail screen" width="220"> | <img src="ReadMeAssets/AppPublicProfileScreen.png" alt="Devicers public profile screen" width="220"> |

### Logos

| Light logo                                                                                           | Dark logo                                                                                          |
| ---------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------- |
| <img src="ReadMeAssets/DevicersLogoLight.png" alt="Devicers logo for light backgrounds" width="360"> | <img src="ReadMeAssets/DevicersLogoDark.png" alt="Devicers logo for dark backgrounds" width="360"> |

## Navigation map

[Download the interactive navigation map here](ReadMeAssets/Sprint4NavigationDiagram.html)

![Navigation map preview](ReadMeAssets/NavigationMapPreview.png)

### Navigation conventions

- **Red routes** represent the navigation actions between the current screens; the adjacent label names the action that triggers each transition.
- **Orange nodes** represent the application screens and **gray nodes** represent navigation-system outcomes, such as exiting the application or returning to the prior screen.
- The Bottom bar leads to Home / Feed, Search Products, Create Review, Activity / Notifications, and Own Profile. Rate Product does not display it.
- System Back exits the application from Home / Feed. From every other destination it returns to the previous entry in the navigation stack.

## Data model

## Object-oriented class diagram

> The diagram is an SVG vector image. Download it or open it directly in a browser to zoom in and inspect the relationships without losing quality.

![Devicers object-oriented class diagram](ReadMeAssets/ClassDiagram.svg)

### Entity-relationship diagram

> The diagram is an SVG vector image. Download it or open it directly in a browser to zoom in and inspect the relationships without losing quality.

![Devicers entity-relationship diagram](ReadMeAssets/DBdiagram.svg)

## Functional requirements

- **FR-01 to FR-04 — Authentication:** create an account with an email address, username, and password; sign in; and restrict features to registered users.
- **FR-05 and FR-06 — Feed:** show reviews from followed accounts, ordered from newest to oldest.
- **FR-07 to FR-11 and FR-30 to FR-32 — Product discovery:** browse products by categories and subcategories; filter by subcategory or brand; search, select, and view product details; and access stores where a product is available.
- **FR-25, FR-26, and FR-33 to FR-36 — Community reviews:** show reviews from other users with author, date, rating, likes, and access to the author's profile; users can hide or delete their own reviews and manage review likes.
- **FR-12 to FR-16 — Ratings and reviews:** rate products using integer values from 0 to 5, write an analysis, allow only one review per user and product, and allow later edits.
- **FR-17, FR-27, FR-28, and FR-37 to FR-40 — Comments:** comment on other users' analyses, including users who are not followed; reply to, edit, hide, or delete own comments; and manage comment likes.
- **FR-29 and FR-44 to FR-45 — Following:** follow or unfollow users, view followers and followed users, and prevent self-following.
- **FR-18 to FR-20 — Profile:** update profile information and photo, delete the account, and view all products rated by the user.
- **FR-21 to FR-23 — Notifications:** notify users about new followers, comments on their reviews, and likes on their reviews.
- **FR-24 — Review reading:** select a review from the feed to view its full content.
- **FR-41 to FR-43 and FR-46 to FR-47 — Account and saved content:** save reviews, view saved reviews, view public profile details, sign out, and hide content without permanently deleting it.

## Technical scope

The project will use MVVM architecture and patterns such as Repository and dependency injection. It includes a REST API with an SQL database, Firebase Authentication and Firestore, automated testing, and Firebase notifications.

### Authentication and Firestore profiles

Registration creates the Firebase Auth account and saves a flat profile in Firestore's `users` collection with its `firebase_uid`. The application keeps numeric user IDs to preserve the existing references. New IDs are consecutive numbers allocated through the `metadata/user_ids` counter (`last_id`). A transaction saves the next ID and its profile together, skips IDs already occupied by migrated users, and retries the UID lookup if another registration advances the counter. Failed registrations do not consume an ID; deleted IDs are not reused. Existing profiles are retrieved by `firebase_uid` and retain their IDs and data.

Sign-in and startup restoration load the linked profile before entering Home. Accounts that previously existed only in Auth receive a profile on their next sign-in. `SessionRepository.currentProfile` exposes the shared profile through a `StateFlow`; `SessionState` includes the Firebase UID, numeric ID and profile. Signing out clears this state and invalidates pending profile loads. Usernames are stored and updated exclusively in Firestore, and the UI reads them from the linked profile. Auth continues to store its photo; photo changes made through `AuthRepository` also update Firestore. Passwords are never copied to Firestore.

New profiles default to `is_active: true`. `createdAt` and `updatedAt` receive the same server timestamp in the creation transaction; later profile changes update only `updatedAt`.

User-specific queries, review creation and ownership checks use the numeric ID of the current session profile. Navigation and avatars use the same identity; there is no fixed user ID or fallback to user 1. The own-profile identity comes from Firestore. Feature data sources that still use Retrofit continue to call the REST API with this session ID.

OwnProfile refreshes `users/{session.userId}` directly from the Firestore server and validates the profile UID before updating the shared session. `OwnProfileRepository` loads its active review cards from the flat `reviews` documents and obtains follower/following counts from Firestore aggregations. These queries use the current session ID and do not call the REST backend. Review removal completed by OwnProfile checks ownership in a Firestore transaction and sets `is_active` to `false`.

Every profile photo is rendered by the shared `ProfileAvatar` using Coil `AsyncImage`. Firestore's `users/{id}.profile_image_url` is the photo source for Home, public profiles, search results, review authors, comments, activity and the current session. `UsersRepository` replaces API photo values with Firestore values; author photos are fetched in batches of up to ten distinct IDs. Missing profiles and empty photos resolve to `no_pfp_icon`, while image-download failures display `pfp_error`. Firebase Auth photos and local drawable names are not used as display fallbacks.
