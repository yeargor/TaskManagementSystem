## Task Management System
It's an application based on Spring Framework technology. In general, the system runs using three containers: one for the Spring application itself, one for the PostgreSQL database that stores tasks and related users, and one for the Keycloak service that handles authentication, authorization, and role management.

## Getting started
1.  **Clone this repository.**

2. **Run the application using Docker Compose:**

```
docker-compose up --build
```

3. **Wait until all services are up and running.**

4. **Visit the** [Swagger panel](http://localhost:8081/swagger-ui/index.html#/ "Swagger panel") t**o view all available APIs.**
5. **To authorize your requests, you must provide the client-id and client-secret that the Spring application uses to interact with Keycloak.**

![Swagger Oauth parameters](https://github.com/yeargor/TaskManagementSystem/blob/57fc16b91d51261090e3550d88c01a2e9ebf2953/readme-images/screen1.jpg)

Don't forget to check all the boxes and supply the correct client-id and client-secret values. These are located in the application.yml file. For your convenience, here are the values you can copy:
```
client-id : yahor-api
client-secret : p8HkF6k5wHzL3qYRCdjjaQEyRCNPKTwO
```

Swagger will redirect you to the Keycloak login page, where you can log in as either an administrator or a client.
## Keycloak users credentials

![Keycloak login page](https://github.com/yeargor/TaskManagementSystem/blob/57fc16b91d51261090e3550d88c01a2e9ebf2953/readme-images/screen2.jpg)

**Administrator**
```
Login: admin
Password: admin
```
**Client**
```
Login : user
Password: user
```
Alternatively, you can create your own user. In our custom realm, users automatically receive the "ROLE_CLIENT" role.

You can visit the [Keycloak admin panel](http://localhost:8080 "Keycloak admin panel")  to modify the properties of the "Yahor" realm and also to assign the "ROLE_ADMIN" role. The credentials for accessing this panel are the same as those for the administrator user of our custom realm.

## Additional Information
Keycloak realm used by the application differs from the common one not only because it includes predefined user templates, but also because it features a custom Event Listener SPI. This custom SPI is used by Keycloak to send requests to the Spring service whenever user information changes and the corresponding database updates are required. User data is stored within Keycloak's local database.

You can visit this Custom Keycloak Event Listener SPI repository [here](https://github.com/yeargor/CustomKeycloakEventListenerSPI/blob/main/src/main/java/org/yeagor/auth/provider/CustomEventListenerProviderFactory.java "here").

Please run tests only when the Docker network is up.
