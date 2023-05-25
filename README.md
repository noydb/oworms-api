<img src="https://github.com/noydb/oworms-ui/blob/develop/src/assets/image/logo.svg"></img> oworms-api
---
[Hosted Application](https://oworms.herokuapp.com) || [Swagger](https://oworms-api.herokuapp.com/swagger-ui/) || [UI Source Code](https://github.com/benj-power/oworms-ui) 

[![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)

---

### Getting Started

1. Run `mvn clean install -Pdev`
2. There must be at least one row in table `settings`
3. There must be at least one valid user in `ow_user` table (whose username will be valid for 6)
4. `cd boot`
5. Run `mvn spring-boot:run -Pdev`
6. u must be passed as a request parameter to all "secure" endpoints (param `u`)
7. bna must be passed as a request parameter to all "secure" endpoints (param `bna`)

#### Optional Configurations
1. Configure valid properties in `application.properties` to send emails
2. Configure valid properties for `oxford.api.url` `oxford.app.id` & `oxford.app.key` ito access the Oxford Dictionaries API 

**Note**: if `mail.disabled=true`, emails logic will be skipped. Also, the bna will be printed in the server logs on startup (You can then use it under the profile section on the UI, along with a valid username to authenticate).

---

### Versioning

1. `git flow release start <version-number>`
2. Bump version in `pom.xml`
3. Bump version in `Procfile`
4. `git flow release finish <version-number>`
5. `git push develop; git push --tags; git co master; git push master`

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

---

### Why this App?
A convenient way to store and find words. I was previously using a simple spreadsheet but as it grew, adding words proved to be inefficient and frankly annoying. Being able to store and find words my way. Sure, one can find any word in existence on the internet, but I wanted a more elegant way to view words. If I google a word and it happens to also be the name of a company, the company will appear first (thanks SEO) and not the word.
