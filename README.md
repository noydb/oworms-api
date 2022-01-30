<img src="https://github.com/benj-power/oworms-ui/blob/develop/src/assets/image/logo.svg"></img>
---
[![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)

<img src="https://github.com/benj-power/oworms-ui/blob/develop/src/asset/image/oh-worm.jpg"></img>

[Explanation of Name](https://memedocumentation.tumblr.com/post/163767097995/explained-oh-worm-meme)

[Swagger Documentation](https://oworms-api.herokuapp.com/swagger-ui/)

[Hosted Application](https://oworms.herokuapp.com)

[UI Source Code](https://github.com/benj-power/oworms-ui)

---

### Running

Follow these steps in order to start the server:

1. Provide values for `oxford.api.url` `oxford.app.id` & `oxford.app.key` in `application.properties`
2. There must be at least one row in `settings`
3. There must be at least one valid user in `ow_user` table
4. Run `mvn clean install`
7. Run `mvn spring-boot:run`
5. A username must be passed as a request parameter to all "secure" endpoints (param name is `u`)
6. A banana must be passed as a request parameter to all "secure" endpoints (param name is `bna`)

---

### Environment Properties

Emails are sent whenever a word is created or updated. If emails should be sent while working locally then please specify the necessary
values in `mail.dev.properties`. Note that emails can be configured to send but turned on and off using `mail.disabled`
in `mail.dev.properties` & `mail.prod.properties`.

The Oxford API values are for configuring communication with their APIs. If one does not possess Oxford API credentials then just replace
the properties with any values (Obviously one will then be unable to call the Oxford retrieve endpoint).
