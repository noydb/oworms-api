<img src="https://github.com/noydb/oworms-ui/blob/develop/src/assets/image/logo.svg"></img> [![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)
---

[Explanation of Name](https://memedocumentation.tumblr.com/post/163767097995/explained-oh-worm-meme)

[Swagger Documentation](https://oworms-api.herokuapp.com/swagger-ui/)

[Hosted Application](https://oworms.herokuapp.com)

[UI Source Code](https://github.com/benj-power/oworms-ui)

---

### Running

Follow these steps in order to start the server:

1. Provide values for `oxford.api.url` `oxford.app.id` & `oxford.app.key` in `application.properties`
2. Run `mvn clean install`
3. There must be at least one row in `app_settings`
4. There must be at least one valid user in `ow_user` table (whose username will be valid for 6)
5. `cd boot`
6. Run `mvn spring-boot:run`
7. u must be passed as a request parameter to all "secure" endpoints (param `u`)
8. bna must be passed as a request parameter to all "secure" endpoints (param `bna`)
9. Optionally, configure valid properties in `mail.dev.properties` to send emails. 

**Note: if you cannot send emails, you MUST get the bna value from the settings row in the DB. you will need to use this for**
**"protected" endpoints. This is a flaw in the design, the app  cannot be used without email (because one won't receive the weekly bna).**
**This will be addressed. For now, grab the value from the DB manually if email sending is not an option**

---

### Environment Properties

Emails are sent whenever a word is created or updated. If emails should be sent while working locally then please specify the necessary
values in `mail.dev.properties`. Note that emails can be configured to send but turned on and off using `mail.disabled`
in `mail.dev.properties` & `mail.prod.properties`.
`mail.recipients` can be a comma separated list of email addresses. all those listed will receive communications whenever a word is 
created, updated, or a new bna is sent

---

### Features
- Viewing words, creating, updating words
- linking tags to words
- liking/favouriting words
- receive an email everytime a word is created or updated
- filtering by any of the available fields
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
