# oworms-api
[![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)

<img src="https://github.com/benj-power/oworms-ui/blob/develop/src/asset/oh-worm.jpg"></img>

[Explanation of Name](https://memedocumentation.tumblr.com/post/163767097995/explained-oh-worm-meme)

[Swagger Documentation](https://oworms-api.herokuapp.com/swagger-ui/)

[Hosted Application](https://oworms.herokuapp.com)

[UI Source Code](https://github.com/benj-power/oworms-ui)

---
### Building
`mvn clean install`

---
### Running

Follow these steps in order to start the server:
1. Provide a value for `permission.key` in `application.properties`
2. Provide values for `oxford.api.url` `oxford.app.id` & `oxford.app.key` 
3. run `mvn spring-boot:run`

Please see `Other` below for more information on these properties.

---
### Other
Emails are sent whenever a word is created or updated. If emails should be sent while working locally then please 
specify the necessary values in `mail.dev.properties`. Note that emails can be configured to send but turned on and off 
using `mail.disabled` in `mail.dev.properties` & `mail.prod.properties`.

`permission.key` "protects" the endpoints that can create and update. `permission_key` must be provided as a parameter when
calling the protected endpoints. `permission.key` can be configured as an environment var when hosting on Heroku. 

The Oxford API values are for configuring communication with their APIs. If one does not possess Oxford API credentials then
just replace the properties with any values (Obviously one will then be unable to call the Oxford retrieve endpoint).

**Note: to deploy this app one must replace all the variable properties in `application.properties` and `mail.prod.properties`**

For further information please refer to the [swagger docs](https://oworms-api.herokuapp.com/swagger-ui/).
