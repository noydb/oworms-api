<img src="https://github.com/noydb/oworms-ui/blob/develop/src/assets/image/logo.svg"></img> oworms-api
---
[Hosted Application](https://oworms.herokuapp.com) || [Swagger](https://oworms-api.herokuapp.com/swagger-ui/) || [UI Source Code](https://github.com/benj-power/oworms-ui) || [Prototype](https://jamieneslotech.invisionapp.com/console/share/KH37M1CTRA/839061901)

[![Maintainability](https://api.codeclimate.com/v1/badges/7bd7122324ce4551a180/maintainability)](https://codeclimate.com/github/noydb/oworms-api/maintainability)

---

### Getting Started

1. Run `mvn clean install`
2. Run `mvn spring-boot:run -Pdev`. This will start the app server on port 8080 (default).
3. `u` (username in `ow_user` table) must be passed as a request parameter to all "secure" endpoints
4. `bna` (bna in `settings` table) must be passed as a request parameter to all "secure" endpoints

`admin` is the default username, you can get the password from `settings` table. Use them under the profile section on the UI to authenticate.

#### Optional Configurations
1. Configure valid properties in `application.properties` to send emails

2. Configure valid properties for `oxford.api.url` `oxford.app.id` & `oxford.app.key` ito access the Oxford Dictionaries API 

**Note**: if `mail.disabled=true`, email operations will be skipped/stubbed.

---

### Versioning

1. `git flow release start <version-number>`
2. Bump version in `pom.xml`
3. Bump version in `Procfile`
4. `git commit -m "<version-number>: <short-description-of-changes>"`
5. `git flow release finish <version-number>`
6. Continue through steps in vim window, use same commit message from 4. on step 2/3.
7. `git push develop; git push --tags; git co master; git push master`

---

### Features
- Viewing words, creating, updating words
- linking tags to words
- liking/favouriting words
- receive an email everytime a word is created or updated
- filtering by any of the available fields, driven by query parameters (e.g. https://oworms.herokuapp.com/o/all?pos=verb,adjective&tags=informal,politics)
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
