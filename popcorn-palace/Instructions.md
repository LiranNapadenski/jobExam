#set up the data base
1. ensure the data base us empty, run: docker-compose down -v
2. run the data base, run: docker-compose up -d

#run the app using : ./mvnw spring-boot:run (on linux)

the base uri is : http://localhost:8080/

#the API
all the entities have constraints, all the constraints are preserved in and after every operation

#movies:
movie format example:
{
    "id": 1,
    "title": "banana boy",
    "genre": "horror",
    "duration": 106,
    "rating": 5.5,
    "releaseYear": 2022
}
constaints: title and genre are less then 30, duration and rating are positive, rating is smaller then 10, releaseYear is after 1888 becuase there were no movies before (i think),


GET /movies/all , returns a list of movies 

POST /movies , the request body needs do look like 
{
    "title": "banana boy",
    "genre": "horror",
    "duration": 106,
    "rating": 5.5,
    "releaseYear": 2022
}

POST /movies/update/{movieTitle}, the request body needs do look like 
{
    "title": "banana boy",
    "genre": "horror",
    "duration": 106,
    "rating": 5.5,
    "releaseYear": 2022
}

DELETE /movies/{movieTitle} delete the movie with the provided title if exists - returns error with a massege if not 

#showtimes:
showtime format example:
{
  "id": 1,
  "theater": "Grand Cinema",
  "start_time": "2025-03-30T18:30:00Z",
  "end_time": "2025-03-30T20:45:00Z",
  "price": 12.99,
  "movieId": 2
}
constaints: "theater" is smaller then 50, start_time is before end_time, price is positive, there is a movie saved with the given movie id

GET /showtimes/{showtimeid} , returns the movies object(json formatted) if exists - returns error with a massege if not 

DELETE /showtimes/{showtimeid}, deletes the show time if exists - returns error with a massege if not 

POST /showtimes/ , the request body needs do look like 
{
  "theater": "Grand Cinema",
  "start_time": "2025-03-30T18:30:00Z",
  "end_time": "2025-03-30T20:45:00Z",
  "price": 12.99,
  "movieId": 2
} * of course the time frame cant contredict any other and still a movie with this ID has to exist

POST /showtimes/update{showtimeid} , the same as the before operation but if a showtime with {showtimeid} didnt exists return error

#booking
{
  "id": 1,
  "seatNumber": 12,
  "userId": "user1234",
  "showtimeId": 3,
  "movieId": 1 *this field has to be null in the request body
}
constaints: seatNumber is positive int, there as to be a showtime with id equal to the given showtimeId, (seatNumber, showtimeId) is unique

POST /booking , the request body needs do look like 
{
  "seatNumber": 12,
  "userId": "user1234",
  "showtimeId": 3
}

#testing
to run aoutometed test, run: ./mvnw test
*this test only check the controllers and are quite shallow, I test manually all the funcuallity if all the componets using postman
*there is a problme in the test - h2 cant use offsetDateTime so the context wont load and fail

#improvments suggetions:
1.in the data base there are relations between the entetis but I didnt define them using JPA in the entities themself this causes some drops in efficiency and problames later when scaling,
and opens the doors to bugs in opreations that could effect difffrent entities - in order to fix it, it is possible to add @OneToMany realtionships,
@joinColoum for refrencing and more

2.some of my validations were without using the jakrata...validations mdoules this also can cause drop in efficency and scalibility

3.testing, my test were shallow and didnt target the repositories and data bases at all and i needed to do manny test manually, it is possible to write more aoutometed test also for the databases and repositories using diffrent moudles then the ones i used.


I hope u find my insturcions enough, and I hope we will meet in the later stages
Liran Napadenski
