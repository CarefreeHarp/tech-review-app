classDiagram

class User{
    +int id
    +String username
    +String email
    +String firebaseUid
    +String biography
    +String profilePicture
    +List DBFollowers
    +List DBFollows
    
    +follow(user)
    +unfollow(user)
    +createReview()
    +editProfile()
    +saveReview()
}

class Article{
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

class Brand{
    +int id
    +String name
}

class Store{
    +int id
    +String name
    +String url
}


class Review{
    +int id
    +int rating
    +String content
    +Date date
    +boolean isActive
    +Date reviewLike
    +edit()
    +delete()
    +hide()
}

class Comment{
    +int id
    +String content
    +Date date
    +boolean hidden
    +Date commentLike

    +edit()
    +delete()
    +reply()
}



class SavedReview{
    +Date date
}

User "1" --> "*" Review : writes
User "1" --> "*" Comment : writes


Article "1" --> "*" Review : has

Category "1" --> "*" Category: subcategory
Category "1" --> "*" Article
Brand "1" --> "*" Article

Article "*" --> "1" Store

Review "1" --> "*" Comment

Comment "1" --> "0..*" Comment : replies to


User "1" --> "*" SavedReview
SavedReview "*" --> "1" Review

