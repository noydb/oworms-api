<img src="https://github.com/noydb/oworms-ui/blob/develop/src/assets/image/logo.svg"></img> [![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)
---

[Swagger Documentation](https://oworms-api.herokuapp.com/swagger-ui/)

[Hosted Application](https://oworms.herokuapp.com)

[UI Source Code](https://github.com/benj-power/oworms-ui)

---

### Running

Follow these steps in order to start the server:

1. Run `mvn clean install -Pdev`
2. There must be at least one row in table `settings`
3. There must be at least one valid user in `ow_user` table (whose username will be valid for 6)
4. `cd boot`
5. Run `mvn spring-boot:run -Pdev`
6. u must be passed as a request parameter to all "secure" endpoints (param `u`)
7. bna must be passed as a request parameter to all "secure" endpoints (param `bna`)
8. Optionally, configure valid properties in `application.properties` to send emails and
9. Provide values for `oxford.api.url` `oxford.app.id` & `oxford.app.key` in to access the oxford API 

**Note: if `mail.disabled=true`, the bna will be printed in the server logs on startup (You can then use it under the 
profile section on the UI, along with a valid username to authenticate).**

---

### Environment Properties

Emails are sent whenever a word is created or updated. If emails should be sent while working locally then please specify the necessary
values in `application.properties`. Note that emails can be configured to send but turned on and off using `mail.disabled` in `application.properties` & `application.dev.properties`. `mail.adminEmailAddress` will receive a mail when an endpoint is invoked more than its configured limit

---

### Features
- Viewing words, creating, updating words
- linking tags to words
- liking/favouriting words
- receive an email everytime a word is created or updated
- filtering by any of the available fields, driven by query parameters (e.g. https://oworms.herokuapp.com/o/worms/all?pos=verb,adjective&tags=informal,politics)
- retrieve a random word
- view statistics on the application & the words
- import spreadsheet of words
- export all words to csv
- fully responsive

### Planned Features
- Full implementation of ui design
- allowing multiple parts of speech and definitions to be linked to one word
- linking of synonyms and antonyms
- ability to delete words
- automated creation of words, adding word with assistance/wizard
- more detailed statistics, daily, weekly stats, graphs, etc
