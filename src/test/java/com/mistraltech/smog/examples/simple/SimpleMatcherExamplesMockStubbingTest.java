package com.mistraltech.smog.examples.simple;

import com.mistraltech.smog.examples.model.Address;
import com.mistraltech.smog.examples.model.Person;
import com.mistraltech.smog.examples.model.PostCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static com.mistraltech.smog.examples.model.PersonBuilder.aPerson;
import static com.mistraltech.smog.examples.simple.matcher.AddressMatcher.anAddressThat;
import static com.mistraltech.smog.examples.simple.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.examples.simple.matcher.PostCodeMatcher.aPostCodeThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class SimpleMatcherExamplesMockStubbingTest {
    private final Person brian = aPerson()
            .withName("Brian")
            .withAge(26)
            .withAddress(new Address(73, new PostCode("out", "in")))
            .build();

    @Test
    public void testMock() {
        Converter converter = mock(Converter.class);

        when(converter.personToName(argThat(is(aPersonThat()
                        .hasName(startsWith("B"))
                        .hasAddress(anAddressThat()
                                        .hasPostCode(aPostCodeThat()
                                                .hasInner("in"))
                        )
        )))).thenReturn("Billie");

        // The mock expectation should match and returns "Billie"

        assertThat(converter.personToName(brian), equalTo("Billie"));
    }

    @Test
    public void testMockFailure() {
        Converter converter = mock(Converter.class);

        when(converter.personToName(argThat(is(aPersonThat()
                        .hasName(startsWith("B")).hasAddress(anAddressThat()
                                        .hasHouseNumber(37)
                                        .hasPostCode(aPostCodeThat()
                                                .hasInner("out"))
                        )
        )))).thenReturn("Billie");

        // The mock expectation should not match so personToName returns null

        assertThat(converter.personToName(brian), Matchers.nullValue());
    }

    private static class Converter {
        public String personToName(Person person) {
            return "Bertie";
        }
    }
}
