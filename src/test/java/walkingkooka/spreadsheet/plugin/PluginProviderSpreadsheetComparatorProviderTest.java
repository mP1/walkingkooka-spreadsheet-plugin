/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.plugin;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.FakePluginProvider;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginProvider;
import walkingkooka.plugin.PluginProviders;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviderTesting;
import walkingkooka.spreadsheet.compare.SpreadsheetComparators;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PluginProviderSpreadsheetComparatorProviderTest implements SpreadsheetComparatorProviderTesting<PluginProviderSpreadsheetComparatorProvider>,
        ToStringTesting<PluginProviderSpreadsheetComparatorProvider> {

    @Test
    public void testWithNullPluginProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> PluginProviderSpreadsheetComparatorProvider.with(null)
        );
    }

    @Test
    public void testSpreadsheetComparator() {
        final SpreadsheetComparator<?> comparator = SpreadsheetComparators.fake();
        final String name = "comparator-123";

        this.spreadsheetComparatorAndCheck(
                PluginProviderSpreadsheetComparatorProvider.with(
                        new FakePluginProvider() {
                            @Override
                            public <T> Optional<T> plugin(final PluginName n,
                                                          final Class<T> type) {
                                checkEquals(name, n.value());
                                checkEquals(SpreadsheetComparator.class, type);
                                return Cast.to(
                                        Optional.of(
                                                comparator
                                        )
                                );
                            }
                        }
                ),
                SpreadsheetComparatorName.with(name),
                comparator
        );
    }

    @Test
    public void testSpreadsheetComparatorInfos() {
        final AbsoluteUrl url = Url.parseAbsolute("https://example/com/testSpreadsheetComparatorInfos");
        final String name = "comparator-123";

        this.spreadsheetComparatorInfosAndCheck(
                PluginProviderSpreadsheetComparatorProvider.with(
                        new FakePluginProvider() {

                            @Override
                            public Set<PluginInfo> pluginInfos() {
                                return Sets.of(
                                        PluginInfo.with(
                                                url,
                                                PluginName.with(name)
                                        )
                                );
                            }
                        }
                ),
                SpreadsheetComparatorInfo.with(
                        url,
                        SpreadsheetComparatorName.with(name)
                )
        );
    }

    @Test
    public void testToString() {
        final PluginProvider pluginProvider = PluginProviders.fake();

        this.toStringAndCheck(
                PluginProviderSpreadsheetComparatorProvider.with(pluginProvider),
                pluginProvider.toString()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PluginProviderSpreadsheetComparatorProvider> type() {
        return PluginProviderSpreadsheetComparatorProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
