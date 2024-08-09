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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginProviderName;
import walkingkooka.plugin.PluginProviderTesting;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviderTesting;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.compare.SpreadsheetComparators;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetComparatorProviderPluginProviderTest implements PluginProviderTesting<SpreadsheetComparatorProviderPluginProvider>,
        SpreadsheetComparatorProviderTesting<SpreadsheetComparatorProviderPluginProvider>,
        SpreadsheetMetadataTesting,
        ToStringTesting<SpreadsheetComparatorProviderPluginProvider> {

    private final static AbsoluteUrl SPREADSHEET_COMPARATOR_INFO_URL = Url.parseAbsolute("https://example.com/SpreadsheetComparatorInfo123");
    private final static String SPREADSHEET_COMPARATOR_INFO_NAME = "Test456";

    private final static Set<SpreadsheetComparatorInfo> INFOS = Sets.of(
            SpreadsheetComparatorInfo.with(
                    SPREADSHEET_COMPARATOR_INFO_URL,
                    SpreadsheetComparatorName.with(SPREADSHEET_COMPARATOR_INFO_NAME)
            )
    );

    private final static SpreadsheetComparatorProvider SPREADSHEET_COMPARATOR_PROVIDER = new SpreadsheetComparatorProvider() {
        @Override
        public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                              final ProviderContext context) {
            return SpreadsheetComparatorProviders.spreadsheetComparators()
                    .spreadsheetComparator(
                            name,
                            context
                    );
        }

        @Override
        public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
            return INFOS;
        }
    };

    private final static PluginProviderName NAME = PluginProviderName.with("Test123");

    private final static AbsoluteUrl URL = Url.parseAbsolute("https://example.com/123");

    @Test
    public void testWithNullSpreadsheetComparatorProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetComparatorProviderPluginProvider.with(
                        null,
                        NAME,
                        URL
                )
        );
    }

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetComparatorProviderPluginProvider.with(
                        SPREADSHEET_COMPARATOR_PROVIDER,
                        null,
                        URL
                )
        );
    }

    @Test
    public void testWithNullUrlFails() {
        assertThrows(
                NullPointerException.class,
                () -> SpreadsheetComparatorProviderPluginProvider.with(
                        SPREADSHEET_COMPARATOR_PROVIDER,
                        NAME,
                        null
                )
        );
    }

    @Test
    public void testSpreadsheetComparatorName() {
        final SpreadsheetComparator<?> comparator = SpreadsheetComparators.date();

        this.spreadsheetComparatorAndCheck(
                this.createPluginProvider(),
                comparator.name(),
                PROVIDER_CONTEXT,
                comparator
        );
    }

    @Test
    public void testSpreadsheetComparatorInfos() {
        this.spreadsheetComparatorInfosAndCheck(
                this.createPluginProvider(),
                SPREADSHEET_COMPARATOR_PROVIDER.spreadsheetComparatorInfos()
        );
    }

    @Test
    public void testPluginInfos() {
        this.pluginInfosAndCheck(
                PluginInfo.with(
                        SPREADSHEET_COMPARATOR_INFO_URL,
                        PluginName.with(SPREADSHEET_COMPARATOR_INFO_NAME)
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createPluginProvider(),
                SPREADSHEET_COMPARATOR_PROVIDER.toString()
        );
    }

    // PluginProvider...................................................................................................

    @Override
    public SpreadsheetComparatorProviderPluginProvider createPluginProvider() {
        return SpreadsheetComparatorProviderPluginProvider.with(
                SPREADSHEET_COMPARATOR_PROVIDER,
                NAME,
                URL
        );
    }

    // SpreadsheetComparatorProvider....................................................................................

    @Override
    public SpreadsheetComparatorProviderPluginProvider createSpreadsheetComparatorProvider() {
        return this.createPluginProvider();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetComparatorProviderPluginProvider> type() {
        return SpreadsheetComparatorProviderPluginProvider.class;
    }


    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
