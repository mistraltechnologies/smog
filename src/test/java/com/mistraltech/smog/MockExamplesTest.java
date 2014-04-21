package com.mistraltech.smog;

import com.mistraltech.smog.example.model.Address;
import com.mistraltech.smog.example.model.Person;
import com.mistraltech.smog.example.model.PostCode;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.mistraltech.smog.example.matcher.AddressMatcher.anAddressThat;
import static com.mistraltech.smog.example.matcher.PersonMatcher.aPersonThat;
import static com.mistraltech.smog.example.matcher.PostCodeMatcher.aPostCodeThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockExamplesTest
{
    private class Converter
    {
        public String personToName(Person person) {
            return "Bertie";
        }
    }

    @Test
    public void testMock()
    {
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
    public void testMockFailure()
    {
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
