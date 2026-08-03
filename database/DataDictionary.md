# Data Dictionary — DeVicers Database

This document describes the structure, attributes, constraints, and relationships of the database.

## `users`

Stores user accounts and profile information.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each user internally. |
| firebase_uid | text | Yes | — | Unique | Firebase Authentication (UID) | Links the Firebase Authentication account to the user stored in the database. |
| email | text | Yes | — | Unique | — | Email address associated with the user account. |
| username | text | Yes | — | Unique | — | Unique username used within the application. |
| biography | text | No | — | Optional | — | Profile biography displayed to other users. |
| profile_image_url | text | No | — | Optional | — | Location where the profile image is stored. |
| is_active | boolean | Yes | — | Default: true | — | Indicates whether the account is active and visible, allowing it to be hidden without deletion. |
| created_at | timestamp | Yes | — | Default: now() | — | Date and time when the account was created. |
| updated_at | timestamp | Yes | — | Default: now(); must be updated when modified | — | Date and time of the most recent account or profile update. |

## `follows`

Records which users follow other users.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| follower_id | int | Yes | Composite PK and FK | Cannot be equal to followed_id | users.id (ON DELETE CASCADE) | Identifies the user who follows another account. |
| followed_id | int | Yes | Composite PK and FK | Cannot be equal to follower_id | users.id (ON DELETE CASCADE) | Identifies the user being followed. |
| created_at | timestamp | Yes | — | Default: now() | — | Date and time when the follow relationship was created. |

## `categories`

Organizes articles into categories and subcategories.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each category or subcategory. |
| parent_category_id | int | No | FK | Optional; forms a unique index together with name | categories.id (ON DELETE SET NULL) | Identifies the parent category. If NULL, the category is a top-level category. |
| name | text | Yes | — | Unique within the same parent category | — | Name of the category or subcategory. |
| description | text | No | — | Optional | — | General description of the category. |

## `brands`

Stores the manufacturers or brands of the articles.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each brand. |
| name | text | Yes | — | Unique | — | Name of the manufacturer or brand, such as Samsung, Lenovo, or Sony. |

## `articles`

Stores the technology products or devices that users can view and review.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each article. |
| category_id | int | Yes | FK | Required | categories.id (ON DELETE RESTRICT) | Identifies the category or subcategory to which the article belongs. |
| brand_id | int | Yes | FK | Required | brands.id (ON DELETE RESTRICT) | Identifies the manufacturer or brand of the article. |
| name | text | Yes | — | Required | — | Commercial name of the article. |
| model | text | No | — | Optional | — | Specific model or reference of the article. |
| description | text | No | — | Optional | — | General description of the article. |
| image_url | text | No | — | Optional | — | Location where the article image is stored. |
| release_date | date | No | — | Optional | — | Release date of the article. |
| specifications | json | No | — | Optional; variable structure | — | Technical specifications of the article stored in JSON format. |
| is_active | boolean | Yes | — | Default: true | — | Indicates whether the article is visible, allowing it to be hidden without deletion. |
| created_at | timestamp | Yes | — | Default: now() | — | Date and time when the article was registered. |
| updated_at | timestamp | Yes | — | Default: now(); must be updated when modified | — | Date and time of the most recent article update. |

## `stores`

Stores general information about the stores where articles can be purchased.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each store. |
| name | text | Yes | — | Unique | — | Name of the store. |
| website_url | text | No | — | Optional | — | Address of the store's main website. |

## `article_stores`

Links each article to the stores where it can be purchased.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| article_id | int | Yes | Composite PK and FK | An article can appear in multiple stores | articles.id (ON DELETE CASCADE) | Identifies the article offered by the store. |
| store_id | int | Yes | Composite PK and FK | A store can sell multiple articles | stores.id (ON DELETE CASCADE) | Identifies the store that offers the article. |
| product_url | text | Yes | — | Required | — | Direct link to the article page in the corresponding store. |

## `reviews`

Stores ratings and reviews written by users about articles.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each review. |
| user_id | int | Yes | FK | Must be unique together with article_id | users.id (ON DELETE CASCADE) | Identifies the user who wrote the review. |
| article_id | int | Yes | FK | Must be unique together with user_id | articles.id (ON DELETE RESTRICT) | Identifies the reviewed article. |
| rating | int | Yes | — | Must be an integer from 0 to 5 | — | Rating given by the user to the article. |
| title | text | No | — | Optional | — | Title of the review. |
| body | text | Yes | — | Required | — | Full review or analysis content. |
| is_active | boolean | Yes | — | Default: true | — | Indicates whether the review is visible, allowing it to be hidden without deletion. |
| created_at | timestamp | Yes | — | Default: now() | — | Date and time when the review was published. |
| updated_at | timestamp | Yes | — | Default: now(); must be updated when modified | — | Date and time of the most recent review update. |

## `comments`

Stores comments posted on reviews and replies to other comments.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each comment. |
| review_id | int | Yes | FK | Required | reviews.id (ON DELETE CASCADE) | Identifies the review on which the comment was posted. |
| user_id | int | Yes | FK | Required | users.id (ON DELETE CASCADE) | Identifies the user who wrote the comment. |
| parent_comment_id | int | No | FK | Optional; must belong to the same review | comments.id (ON DELETE SET NULL) | Identifies the comment being replied to. If NULL, it is a top-level comment. |
| body | text | Yes | — | Required | — | Content of the comment. |
| is_active | boolean | Yes | — | Default: true | — | Indicates whether the comment is visible, allowing it to be hidden without deletion. |
| created_at | timestamp | Yes | — | Default: now() | — | Date and time when the comment was posted. |
| updated_at | timestamp | Yes | — | Default: now(); must be updated when modified | — | Date and time of the most recent comment update. |

## `review_likes`

Records likes given by users to reviews.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each like. |
| user_id | int | No | FK | Must be unique together with review_id; may become NULL | users.id (ON DELETE SET NULL) | Identifies the user who gave the like. It remains NULL if the account is deleted. |
| review_id | int | Yes | FK | Must be unique together with user_id | reviews.id (ON DELETE CASCADE) | Identifies the review that received the like. |

## `comment_likes`

Records likes given by users to comments.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| id | int | Yes | PK | Auto-increment | — | Uniquely identifies each like. |
| user_id | int | No | FK | Must be unique together with comment_id; may become NULL | users.id (ON DELETE SET NULL) | Identifies the user who gave the like. It remains NULL if the account is deleted. |
| comment_id | int | Yes | FK | Must be unique together with user_id | comments.id (ON DELETE CASCADE) | Identifies the comment that received the like. |

## `review_bookmarks`

Records the reviews saved by each user.

| Attribute | Type | Required | Key | Properties | Reference | Description |
|---|---|---|---|---|---|---|
| user_id | int | Yes | Composite PK and FK | Prevents the same review from being saved twice | users.id (ON DELETE CASCADE) | Identifies the user who saved the review. |
| review_id | int | Yes | Composite PK and FK | Prevents the same review from being saved twice | reviews.id (ON DELETE CASCADE) | Identifies the review saved by the user. |
