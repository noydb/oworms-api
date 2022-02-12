<img src="https://github.com/benj-power/oworms-ui/blob/develop/src/assets/image/logo.svg"></img> [![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)
---

[Explanation of Name](https://memedocumentation.tumblr.com/post/163767097995/explained-oh-worm-meme)

[Swagger Documentation](https://oworms-api.herokuapp.com/swagger-ui/)

[Hosted Application](https://oworms.herokuapp.com)

[UI Source Code](https://github.com/benj-power/oworms-ui)

---

### Running

Follow these steps in order to start the server:

1. Provide values for `oxford.api.url` `oxford.app.id` & `oxford.app.key` in `application.properties`
2. There must be at least one row in `settings`
3. There must be at least one valid user in `ow_user` table (whose username will be valid for 6.)
4. Run `mvn clean install`
5. Run `mvn spring-boot:run`
6. A username must be passed as a request parameter to all "secure" endpoints (param name is `u`)
7. A banana must be passed as a request parameter to all "secure" endpoints (param name is `bna`)
8. Optionally, configure valid properties in `mail.dev.properties` to send emails. 

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

The Oxford API values are for configuring communication with their APIs. If one does not possess Oxford API credentials then just replace
the properties with any values. (Obviously one will then be unable to call the Oxford retrieve endpoint)

---

### Features
- Viewing words, creating, updating words
- linking tags to words
- receive an email everytime a word is created or updated
- filtering by any of the available fields
- search the oxford dictionaries api
- retrieve a random word
- view statistics on the application
- import spreadsheet of words
- export all words to csv

### Planned Features
- automated creation of words
- adding word while leveraging oxford api
- more detailed statistics, daily, weekly stats, graphs, etc
- linking of synonyms and antonyms
- ability to delete words
- allowing multiple parts of speech and definitions to be linked to one word
- mobile responsiveness
- Full implementation of ui design