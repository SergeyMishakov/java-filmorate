# java-filmorate

Ссылка к схеме:

![This is an image](https://github.com/SergeyMishakov/java-filmorate/blob/main/%D0%A1%D1%85%D0%B5%D0%BC%D0%B0%20%D0%91%D0%94.png)

Примеры запросов к базе:

//Получение списка всех фильмов
SELECT *
FROM Film

//Получение фильма по ID
SELECT * 
FROM Film
WHERE film_id = 1

//Получение списка лайков к фильму
SELECT *
FROM like_list
WHERE film_id = 1

//Получение списка друзей
SELECT *
FROM friend_list
WHERE user_id = 1
