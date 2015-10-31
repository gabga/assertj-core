/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.maps;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.error.ShouldBe;
import org.assertj.core.error.ShouldBeEqual;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.MapRepresentation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Maps#assertEqual(AssertionInfo, Map, Map, ComparisonStrategy)}</code>.
 *
 * @author Vojislav Marinkovic
 */
public class Maps_assertEqual_Test extends MapsBaseTest {
    @Override
    @Before
    public void setUp() {
        super.setUp();
        actual = new HashMap<String, String>();
        actual.put("a", "1");
        actual.put("b", "2");
        actual.put("c", "3");
    }

    @Test
    public void should_pass_if_maps_are_equal() {
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("c", "3");
        maps.assertEqual(someInfo(), actual, expected, StandardComparisonStrategy.instance());
    }

    @Test
    public void should_fail_if_map_entry_values_are_not_equal() throws NoSuchFieldException, IllegalAccessException {
        AssertionInfo info = someInfo();
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("c", "4");
        try {
            maps.assertEqual(someInfo(), actual, expected, StandardComparisonStrategy.instance());
        } catch (AssertionError e) {
            ArgumentCaptor<ShouldBeEqual> shouldBeEqualArgumentCaptor = ArgumentCaptor.forClass(ShouldBeEqual.class);
            verify(failures).failure(eq(info), shouldBeEqualArgumentCaptor.capture());
            ShouldBeEqual shouldBeEqual = shouldBeEqualArgumentCaptor.getValue();

            Field representation = shouldBeEqual.getClass().getDeclaredField("representation");
            representation.setAccessible(true);
            MapRepresentation mapRepresentation = (MapRepresentation)representation.get(shouldBeEqual);
            Field unequalEntryByValueField = mapRepresentation.getClass().getDeclaredField("unequalEntryByValue");
            unequalEntryByValueField.setAccessible(true);
            Set unequalEntryByValue = (Set) unequalEntryByValueField.get(mapRepresentation);
            assertThat(unequalEntryByValue.size()).isEqualTo(1);
            assertThat(unequalEntryByValue).contains("c");
            return;
        }
        failBecauseExpectedAssertionErrorWasNotThrown();
    }

    @Test
    public void should_fail_if_map_entry_keys_are_not_equal() throws NoSuchFieldException, IllegalAccessException {
        AssertionInfo info = someInfo();
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("a", "1");
        expected.put("b", "2");
        expected.put("d", "3");
        try {
            maps.assertEqual(someInfo(), actual, expected, StandardComparisonStrategy.instance());
        } catch (AssertionError e) {
            ArgumentCaptor<ShouldBeEqual> shouldBeEqualArgumentCaptor = ArgumentCaptor.forClass(ShouldBeEqual.class);
            verify(failures).failure(eq(info), shouldBeEqualArgumentCaptor.capture());
            ShouldBeEqual shouldBeEqual = shouldBeEqualArgumentCaptor.getValue();

            Field representation = shouldBeEqual.getClass().getDeclaredField("representation");
            representation.setAccessible(true);
            MapRepresentation mapRepresentation = (MapRepresentation)representation.get(shouldBeEqual);
            Field unequalEntryByKeyField = mapRepresentation.getClass().getDeclaredField("unequalEntryByKey");
            unequalEntryByKeyField.setAccessible(true);
            Set unequalEntryByKey = (Set) unequalEntryByKeyField.get(mapRepresentation);
            assertThat(unequalEntryByKey.size()).isEqualTo(2);
            assertThat(unequalEntryByKey).contains("c");
            assertThat(unequalEntryByKey).contains("d");
            return;
        }
        failBecauseExpectedAssertionErrorWasNotThrown();
    }
}
