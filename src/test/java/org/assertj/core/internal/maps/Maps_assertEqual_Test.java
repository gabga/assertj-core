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
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Maps;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.MapRepresentation;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Maps#assertEqual(AssertionInfo, Map, Map, ComparisonStrategy)}</code>.
 *
 * @author Vojislav Marinkovic
 */
public class Maps_assertEqual_Test extends MapsBaseTest {

    @Test
    public void should_pass_if_maps_are_equal() {
        Map<String, Integer> actual = new HashMap<String, Integer>();
        actual.put("a", 1);
        actual.put("b", 2);
        actual.put("c", 3);
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        maps.assertEqual(someInfo(), actual, expected, StandardComparisonStrategy.instance());
    }

    @Test
    public void should_fail_if_maps_are_not_equal() {
        AssertionInfo info = someInfo();
        Map<String, Integer> expected = new HashMap<String, Integer>();
        expected.put("a", 1);
        expected.put("b", 2);
        expected.put("c", 3);
        Map<String, Integer> actual = new HashMap<String, Integer>();
        actual.put("a", 1);
        actual.put("b", 2);
        actual.put("c", 4);
        try {
            maps.assertEqual(someInfo(), actual, expected, StandardComparisonStrategy.instance());
        } catch (AssertionError e) {
            //TODO
            verify(failures).failure(info, shouldBeEqual(actual, expected, new MapRepresentation()));
            return;
        }
        failBecauseExpectedAssertionErrorWasNotThrown();
    }
}