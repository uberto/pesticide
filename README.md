[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ubertob.pesticide/pesticide-core/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ubertob.pesticide/pesticide-core)

# Pesticide
A Library To Write Domain-Driven Tests, written in Kotlin on top of Junit5.

## What are DDT?
Unit-Tests are very useful but they work on a small scale, we can test only a few objects or functions in this way.

We also need a way to test our full application end-to-end in order to be sure to be on the right track. We can do that simulating user clicking on the web page but we need a more abstract way to represent it, otherwise, tests would become quickly very hard to understand

Nat Pryce invented the Domain-Driven style of tests when he got tired of tests written in a way "click here and then click there".

[video](https://www.youtube.com/watch?v=Fk4rCn4YLLU)

They also are influenced by Serenity and the Screenplay pattern by Anthony Marcano.

[article](https://www.infoq.com/articles/Beyond-Page-Objects-Test-Automation-Serenity-Screenplay/)

They have been named from the concern that tests should concentrate on the business domain and he named this style DDT also as a pun since they are quite efficient in killing bugs (like the pesticide).
We also aim to use the same terms both in our tests and in the conversation with the business people. In this way, we can facilitate the communication between people working on the software and the business domain experts. This practice is sometimes called the ubiquitous language, [see](https://martinfowler.com/bliki/UbiquitousLanguage.html)

The goal is to describe our stories as interactions between actors [see why here](https://www.infoq.com/presentations/pragmatic-personas/) and as an abstraction of our system. Using different system implementations we can reach these benefits:

- Test the functionality works both end-to-end and in the in-memory domain
- Document the story using a language close to business
- Describing test without using UI details (click here/insert text there)
- Make sure there is no business logic in the infrastructure layer
- Make sure there is no infrastructure details in the business logic

## How does Pesticide work?
See some Kotlin examples in the pesticide-example project, and some Java examples in the pesticide-example-java project.

- CalculatorDDT: simplest example
- FablesDDT: in-memory only example to see the narrative flow
- GooglePageDDT: http/html only example to see how to integrate with WebDriver
- PetShopDDT: example on how to test restful apis
- StackDDT: simple example in Java

## What does it look like?
![tests running](docs/FablesTestRunning.png)

## Dependency declaration
Maven
```
<dependency>
  <groupId>com.ubertob.pesticide</groupId>
  <artifactId>pesticide-core</artifactId>
  <version>0.9.6</version>
</dependency>
```

Gradle
```
testImplementation 'com.ubertob.pesticide:pesticide-core:0.9.6'
```