package com.mistraltech.smog;

import com.mistraltech.smog.example.model.Address;
import com.mistraltech.smog.example.model.Person;
import com.mistraltech.smog.example.model.Phone;
import com.mistraltech.smog.example.model.PostCode;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Test;

import static com.mistraltech.smog.example.matcher.AddressMatcher.anAddressThat;
import static com.mistraltech.smog.example.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.example.matcher.PhoneMatcher.aPhoneThat;
import static com.mistraltech.smog.example.matcher.PostCodeMatcher.aPostCodeThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

public class MatcherExamples {

    /*
        Person p1 = new Person("bob", 21, new Address(8, new PostCode("cv1", "1aa")));
        Person p2 = new Person("bob", 21, new Address(8, new PostCode("cv1", "1aa")));
     */

    @Test
    public void testBothMatcher() throws Exception {
        assertThat("abc", CombinableMatcher.<String>both(equalTo("abc")).and(equalTo("bc")));
    }

    @Test
    public void testAnyOfMatcher() throws Exception {
        assertThat("abc", anyOf(equalTo("ab"), equalTo("bc"), equalTo("ac")));
    }

    @Test
    public void testDeepCompositeCustomMatcher() {
        assertThat(new Person("bob", 36, new Address(21, new PostCode("out", "in"))),
                is(aPersonThat()
                        .hasName("bob")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner(startsWith("i"))
                                        .hasOuter(containsString("y")))))
        );
    }

    @Test
    public void testDeepCompositeCustomMatcherWithManyMismatches() {
        assertThat(new Person("bob", 36, new Address(22, new PostCode("out", "in"))),
                is(aPersonThat()
                        .hasName("obo")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner(startsWith("x"))
                                        .hasOuter(containsString("y")))))
        );
    }

    @Test
    public void testDeepCompositeCustomMatcherWithNull() {
        assertThat(new Person("bob", 36, null),
                is(aPersonThat()
                        .hasName("obo")
                        .hasAddress(anAddressThat()
                                .hasHouseNumber(21)
                                .hasPostCode(aPostCodeThat()
                                        .hasInner(startsWith("x"))
                                        .hasOuter(containsString("y")))))
        );
    }

    @Test
    public void testEmptyListMatching() {
        assertThat(new Person("bob", 36, null, new Phone("123", "456456"), new Phone("123", "123123")),
                is(aPersonThat()
                        .hasPhoneList(IsEmptyCollection.<Phone>empty()))
        );
    }

    @Test
    public void testListSizeMatching() {
        assertThat(new Person("bob", 36, null, new Phone("123", "456456"), new Phone("123", "123123")),
                is(aPersonThat()
                        .hasPhoneList(IsCollectionWithSize.<Phone>hasSize(1)))
        );
    }

    @Test
    public void testListContainsMatching() {
        assertThat(new Person("bob", 36, null, new Phone("123", "456456"), new Phone("123", "123123")),
                is(aPersonThat()
                        .hasPhoneList(contains(
                                aPhoneThat().hasCode("123").hasNumber("456456"),
                                aPhoneThat().hasCode("123").hasNumber("123321"))))
        );
    }
}
