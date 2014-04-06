# smog

Sparse Matching of Object Graphs - an extension to Hamcrest

## Summary

Problem:

You are writing JUnit-style tests for operations that update an object graph in various ways. In each test, you want to assert that specific properties of the objects in the object graph are correct.

Solution:

You write a matcher for each class in the object graph based on the smog library, then compose instances of those matcher classes into a matcher object tree for each test. Each matcher tree matches just the properties you care about for that test.

You then add a single Hamcrest assertion to each of your tests asserting that the actual resulting object graph matches your expected object graph.

If any of the properties do not match, the assertion will fail with a message that allows you to easily identify which property or properties within the object graph didn't match and what their expected and actual values were.

## Example Matcher Usage

```
@Test
public void canConstructPerson()
{
  Person bob = new Person("bob", 23);
  
  assertThat(bob, is(aPersonThat().hasForename("bob").hasAge(23));
}

// ok, that was only a single object. How about this...

@Test
public void canAddTelephoneNumber()
{
  Person bob = new Person("bob", 23);
  
  bob.addTelephone("01234", "567567")  // constructs and assigns a Telephone object to Person
  
  assertThat(bob, is(aPersonThat().hasTelephone(aTelephoneThat().hasAreaCode("01234").hasNumber("567567"))));
}

// Sample output for a mismatch, assuming area code was incorrectly reversed in the addTelephone method:


java.lang.AssertionError
Expected: is a Person that (has telephone (a Telephone that (has area code ("01234") and number ("567567"))))
     but: telephone.areaCode was "43210" (expected "01234")
```
## Issues/Suggestions

Smog currently depends on slf4j. It would be good to factor this out.

## Why do I need smog?

Of course, you can easily write matcher classes that match the properties of an object without using smog. But what happens when the match fails? You may get a message containing a java object reference for the object that didn't match. If you're lucky (by which I mean you and your colleagues follow good coding practices) you instead get a toString() output of all the properties of an object. You are then left to work out which property caused the failure. This might not be too bad for an object with a couple of properties, but as the number of properties grows it can seriously slow you down (and give you a cracking headache).

What smog does is track the path to each property so that when a match fails, it can give you a message that describes exactly which properties didn't match and why. If multiple properties didn't match, it lists all of them. And it works within the Hamcrest framework so that if the original assertion reads well, the failure message should read well too.

## Couldn't I just use AssertEquals in my tests?

AssertEquals is fine for primitive values. If you're using it to compare objects though, there are some issues. Firstly you must fully construct your expectation object. This means setting values even if you don't care about them in the test. This adds noise to the test, obscuring the intent, and makes the test fragile. Also, equals() may not be overridden on the object you are testing, or may not mean what you think. Persistent entities often define equals() to mean something other than "do all the properties on these two objects match". Even if equals() means that now, will it always mean that?

You could of course write test helper methods with a test-specific implementation of equals(), but you will still have the test noise issue.

## Couldn't I just write multiple assertions?

You could write a separate assertion for each property you care about. But then, only the first failure will be reported. And your code will also be more verbose.

## Is this style of testing a good idea?

Not necessarily - opinions vary. Certainly it is possible to write complex test expectations that are hard to read and maintain. And it may lead to tests that do too much. Basically, it is up to you to use it wisely.

## I'm using matchers in mocks - how does smog help?

todo

## Acknowledgments

This project builds on the work and ideas of developers of the Titan Pricing Management application at
Black Pepper Software (http://blackpepper.co.uk/).

The project extends the Hamcrest Matcher library at http://hamcrest.org/ and relies heavily on the mechanism introduced in the TypeSafeDiagnosingMatcher.


