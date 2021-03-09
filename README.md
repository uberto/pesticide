[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.ubertob.pesticide/pesticide-core/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.ubertob.pesticide/pesticide-core)

# Pesticide

A Library To Write Domain-Driven Tests, written in Kotlin (but it works in Java projects as well).

I wrote a blog post about DDT and Pesticide:
[Here](https://medium.com/javarevisited/beyond-traditional-acceptance-tests-79cbcee63eda)

My presentation about Pesticide:
[![Watch the video](https://secure.meetupstatic.com/photos/event/2/2/c/0/highres_490268896.jpeg)](https://youtu.be/cUNVTXf6LxY)

## TLDR; Short version

The name come from the concern that acceptance tests should be written using business domain terms. DDT is also an apt name since they are quite efficient in killing bugs (like the pesticide)

The goal is to describe our stories as interactions between actors [see why here](https://www.infoq.com/presentations/pragmatic-personas/) and as an abstraction of our system. Using different system implementations we can reach these benefits:

- Test the functionality works both end-to-end and in the in-memory domain
- Document the story using a language close to the business
- Describing test without using UI details (click here/insert text there)
- Make sure there is no business logic in the infrastructure layer
- Make sure there is no infrastructure details in the business logic

## How does Pesticide work?
See some Kotlin examples in the pesticide-example project, and some Java examples in the pesticide-example-java project.

- CalculatorDDT: the simplest example
- FablesDDT: an in-memory only example to see how the narrative flows
- GooglePageDDT: a http/html only example to see how to integrate with WebDriver
- PetShopDDT: a full example on how to test restful apis using Http4k
- StackDDT: a simple example to see how to use it from Java

## What does it look like?

[FablesDDT example code](pesticide-examples/src/test/kotlin/com/ubertob/pesticide/examples/fables/FablesDDT.kt)

![tests running](docs/FablesTestRunning.png)

## Dependency declaration
Maven
```
<dependency>
  <groupId>com.ubertob.pesticide</groupId>
  <artifactId>pesticide-core</artifactId>
  <version>1.4.1</version>
</dependency>
```

Gradle
```
testImplementation 'com.ubertob.pesticide:pesticide-core:1.4.1'
```

## What's new?

* 2021/03/09 v.1.4.1
  - fix for a bug when running in Windows

* 2021/02/04 v.1.4
  - switched to Kotlin 1.4

* 2020/07/19 v.1.3.1
  - fix for bug with WIP exceptions

* 2020/07/18 v.1.3
  - better support for Work-In-Progress tests. Now it shows when steps are passing in WIP tests and report the reason
    together with the due date. If all steps are passing, the test will now fail (instead of just being skipped) to
    force the removal of the WIP marker.

* 2020/05/21 v.1.2
  - added actor context. It's now possible to store and retrieve data between steps. See for example `PetBuyer.kt`.


## FAQ

- Is it safe to use Pesticide in my project?

    We are using on a big project mission critical since 2019. In any case it's only test code and it should not be deployed in production. 
    I'll will support it in the foreseeable future but in any case you can always fork and adapt to your needs since it's open source.

- Why the theater metaphor?
    
    It came up quite naturally since we aim to *play* our user-stories in our DDT.

- Why actors at all?

    Writing the stories based on actors help us to focus on the actual end-user perspective. We discovered that writing tests in this way make them easier to maintain and more useful to guide our design.
    Moreover actor steps are the best place to put the assertions on the domain state in a way that works for all the interpreters.

- Why a test for step?
    
    In this way is much easier to see at what point it fails and jump directly to the specific line of code from the IDE.

- Why not Given-When-Then format?
    
    Although GWT is a good format for unit-tests in our experience it doesn't work well with stories, that can be longer and with multiple check points.

- Why a setting block?
    
    It makes more clear what we want to test and what must be the state of the system at the beginning.

## Acknowledgement

I want to thank first Nat Pryce for having introduced me to the DDT concept and the long hours spent pairing together that helped me to finally understand it.

We also stealed a lot of good ideas to Antony Marcano's Serenity project.

Duncan McGregor's Minutest project was also a big inspiration for me, both as api design and technical implementation.

Last but not least, my current team: Asad Manji, Tom Power, Andrew Couchman, Andrew Gray and the rest of the developers for suggestions, contribution and in general having great time working together.