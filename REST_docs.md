# QuackR

# Как да използвам REST-API-то?

Първо трябва да създадем потребител.

Всеки потребител има име, парола, рейтинг и роля (правомощия)

Името е уникално за всеки потребител.
Рейтингът е просто някакво число, което май няма да изплозваме, защото няма време.
Ролята (правомощията) са съответно USER и ADMIN.

Потребител се създава с POST Request към `/users`.

Например:

`POST /users`
```json
{
	"username": "max",
	"password": "testtest1",
	"rating": 0,
	"role": "USER"
}
```

Първият създаден (регистрирал се) потребител получава автоматично ролята ADMIN. 
Потребители регистрирали се след това получават ролята USER и могат да получат ролят ADMIN
единствено от друг ADMIN.

Отговорът, който получаваме изглежда по този начин:

```json
{
    "id": 1,
    "username": "max",
    "rating": 0,
    "role": "ADMIN"
}
```

След като сме се регистрирали, трябва да се лог-ин-нем.
Това става чрез `Request` към `/users/login`

Например:

`POST /users/login`
```json
{
	"username": "max",
	"password": "testtest1"
}
```

Отговорът, който получаваме изглежда по този начин:

```json
{
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1NjAxNjM1MzgsInVzZXJuYW1lIjoibWl0a28ifQ.kJs13dubAFIIoP-J_7cvvUtV8ku_TDfZIbfqRHGMyvo"
}
```

Token-ът е необходим за аутентификация във всички Request-и освен логин и регистрация.
Трябва да бъде добаве към Request Header-ите под полето `Authorization`.

### Редактиране на потребител:

`POST /users/1`
```
{
	"username": "max",
	"password": "testtest1",	
	"rating": 30,
	"role": "ADMIN"
}
```


Отговорът, който получаваме изглежда по този начин:

```json
{
    "id": 1,
    "username": "max",
    "rating": 30,
    "role": "ADMIN"
}
```

Потребител с ролята ADMIN може да редактира който и да е потребител.
Потребител с ролята USER може да редактира само себе си, като няма право да променя правомощията си.

### Изтриване на потребител:

`DELETE /users/1`

### Получаване на потребители:

`GET /users/1` и `GET /users`

# Грешки

Почти всички грешки, които получаваме от сървъра имат следният вид:

```json
{
    "errorMessage": "описание на грешката"
}
```

Възможи грешки:

`404 NOT FOUND` - Потребителят/Коментарът/Събитието не е намерен.

`400 BAD REQUEST` - Грешка в изпратеният JSON (липсващи скоби, полета). Има следният вид:

```
[
    {
        "message": "The description may not be null",
        "messageTemplate": "The description may not be null",
        "path": "EventController$$EnhancerBySpringCGLIB$$e1b3cbb7.editEvent.resource.description",
        "invalidValue": null
    },
    {
        "message": "The title may not be null",
        "messageTemplate": "The title may not be null",
        "path": "EventController$$EnhancerBySpringCGLIB$$e1b3cbb7.editEvent.resource.title",
        "invalidValue": null
    }
]
```

`403 FORBIDDEN` - Потребителят няма правомощия за да извърши действието.

`401 FORBIDDEN` - Потребителят не може да бъде аутентифициран (няма логин).


# Събития (Events)


### Създаване:

`POST /events/user/{userId}`
```json
{
	"title": "BBQ",
	"date": "2019-06-01",
	"location": "Max",
	"description": "A BBQ",
	"attendeeLimit": 20,
	"public": true
}
```

Отговор:

```json
{
    "id": 1,
    "title": "BBQ",
    "location": "Max",
    "date": "2019-06-01T00:00:00.000+0000",
    "description": "A BBQ",
    "attendeeLimit": 20,
    "attendees": [],
    "comments": [],
    "public": true
}
```

### Редактиране:

`POST /events/{eventId}`
```json
{
	"title": "BBQ",
	"date": "2019-06-01",
	"location": "Max",
	"description": "A summer BBQ",
	"attendeeLimit": 30,
	"public": true
}
```

Отговор:

```json
{
    "id": 1,
    "title": "BBQ",
    "location": "Max",
    "date": "2019-06-01T00:00:00.000+0000",
    "description": "A summer BBQ",
    "attendeeLimit": 30,
    "attendees": [],
    "comments": [],
    "public": true
}
```

### Получаване:

`GET /events` и `GET /events/{eventId}`


### Изтриване:

`DELETE /events/{eventId}`

### Добавяне/премахване на участници:

`POST /events/{eventId}/add` и `POST /events/{eventId}/remove`
```json
[
	{
		"id": 1,
		"username": "max",
		"rating": 0,
		"role": "ADMIN"
	},
    {
		"id": 1,
		"username": "max",
		"rating": 0,
		"role": "ADMIN"
	}
]
```

Тук също важи, че потребители с правомощия ADMIN могат да променят/изтриват/добавят участници към което и да е събитие.

За коментарите е горе-долу същото, но мисля, че няма да стигнем до там изобщо, та да го ограничим първо до събития (пък и ако тва не става, ще 
ги представим като съобщения и толкоз).


