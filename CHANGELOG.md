<h2 class="github">Changelog</h2>

This list is not currently intended to be all-encompassing - it will document major and breaking API 
changes with their rationale when appropriate:

### v0.9.12
- going back in using api for JUnit dependency because of IntelliJ resolution
- settings block doesn't require a result
- if no protocols is selected, the test will fail
- changed protocol InMemoryHubs to DomainOnly and PureHttp to Http

### v0.9.11
- using implementation instead of api for JUnit dependency because of IntelliJ resolution

### v0.9.10
- changed name of class BoundedContextInterpreter to DomainInterpreter
- added ActorWithContext class for Actors that must store and retrive info during the test

### v0.9.9
- changed name of class DomainUnderTest to BoundedContextInterpreter

### v0.9.8
- changed DomainUnderTest.isReady() to prepare()

### v0.9.7
- added actor name to the step auto naming

### v0.9.6
- pass the domain from one step to the next
- improved the step auto naming

### v0.9.5
- added junit5 as api dependency

### v0.9.4
- made DdtActor abstract class instead of Interface, to avoid using JvmDefault

### v0.9.3
- others small changes to make Java tests easier

### v0.9.2
- added Java examples and made api more friendly to Java

### v0.9.1
- initial version

