package com.mistraltech.smog.examples.simple;

import com.mistraltech.smog.examples.simple.model.Address;
import com.mistraltech.smog.examples.simple.model.Person;
import com.mistraltech.smog.examples.simple.model.PostCode;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.mistraltech.smog.examples.simple.matcher.AddressMatcher.anAddressThat;
import static com.mistraltech.smog.examples.simple.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.simple.matcher.PostCodeMatcher.aPostCodeThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleMatcherExamplesMockStubbingTest
{
    private class Converter {
        public String personToName(Person person) {
            return "Bertie";
        }
    }

    @Test
    public void testMock() {
        Converter converter = mock(Converter.class);

        Person person = new Person("Brian", 26, new Address(73, new PostCode("out", "in")));

        when(converter.personToName(argThat(is(aPersonThat()
                        .hasName(startsWith("B")).hasAddress(anAddressThat()
                                        .hasPostCode(aPostCodeThat().hasInner("in"))
                        )
        )))).thenReturn("Billie");

        assertThat(converter.personToName(person), equalTo("Billie"));
    }

    @Test
    public void testMockFailure() {
        Converter converter = mock(Converter.class);

        Person person = new Person("Brian", 26, new Address(73, new PostCode("out", "in")));

        when(converter.personToName(argThat(is(aPersonThat()
                        .hasName(startsWith("B")).hasAddress(anAddressThat()
                                        .hasHouseNumber(37)
                                        .hasPostCode(aPostCodeThat().hasInner("out"))
                        )
        )))).thenReturn("Billie");

        assertThat(converter.personToName(person), Matchers.nullValue());
    }

}
