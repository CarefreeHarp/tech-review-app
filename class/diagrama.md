classDiagram

class User{
    +int id
    +String username
    +String email
    +String password
    +String biography
    +String profilePicture

    +follow(user)
    +unfollow(user)
    +createReview()
    +editProfile()
    +saveReview()
}

class Product{
    +int id
    +String name
    +String model
    +String description
    +Date releaseDate
    +String image

    +getAverageRating()
    +getReviews()
}

class Category{
    +int id
    +String name
}

class Subcategory{
    +int id
    +String name
}

class Brand{
    +int id
    +String name
}

class Store{
    +int id
    +String name
    +String url
}

class Availability{
    +String productUrl
}

class Review{
    +int id
    +int rating
    +String content
    +Date date
    +boolean hidden

    +edit()
    +delete()
    +hide()
}

class Comment{
    +int id
    +String content
    +Date date
    +boolean hidden

    +edit()
    +delete()
    +reply()
}

class Notification{
    +int id
    +String type
    +String message
    +Date date
    +boolean read

    +markAsRead()
}

class Follow{
    +Date date
}

class SavedReview{
    +Date date
}

class ReviewLike{
    +Date date
}

class CommentLike{
    +Date date
}

User "1" --> "*" Review : writes
User "1" --> "*" Comment : writes
User "1" --> "*" Notification : receives

Product "1" --> "*" Review : has

Category "1" --> "*" Subcategory
Subcategory "1" --> "*" Product
Brand "1" --> "*" Product

Product "1" --> "*" Availability
Store "1" --> "*" Availability

Review "1" --> "*" Comment

Comment "1" --> "0..*" Comment : replies to

User "1" --> "*" Follow : follows
Follow "*" --> "1" User : followed

User "1" --> "*" SavedReview
SavedReview "*" --> "1" Review

User "1" --> "*" ReviewLike
ReviewLike "*" --> "1" Review

User "1" --> "*" CommentLike
CommentLike "*" --> "1" Comment