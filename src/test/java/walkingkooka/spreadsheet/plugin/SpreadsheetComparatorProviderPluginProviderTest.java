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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginProviderName;
import walkingkooka.plugin.PluginProviderTesting;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviderTesting;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProviders;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorSelector;
import walkingkooka.spreadsheet.compare.SpreadsheetComparators;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetComparatorProviderPluginProviderTest implements PluginProviderTesting<SpreadsheetComparatorProviderPluginProvider>,
    SpreadsheetComparatorProviderTesting<SpreadsheetComparatorProviderPluginProvider>,
    SpreadsheetMetadataTesting,
    ToStringTesting<SpreadsheetComparatorProviderPluginProvider> {

    private final static AbsoluteUrl SPREADSHEET_COMPARATOR_INFO_URL = Url.parseAbsolute("https://example.com/SpreadsheetComparatorInfo123");
    private final static String SPREADSHEET_COMPARATOR_INFO_NAME = "Test456";

    private final static SpreadsheetComparatorInfoSet INFOS = SpreadsheetComparatorInfoSet.EMPTY.concat(
        SpreadsheetComparatorInfo.with(
            SPREADSHEET_COMPARATOR_INFO_URL,
            SpreadsheetComparatorName.with(SPREADSHEET_COMPARATOR_INFO_NAME)
        )
    );

    private final static SpreadsheetComparatorProvider SPREADSHEET_COMPARATOR_PROVIDER = new SpreadsheetComparatorProvider() {
        @Override
        public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorSelector selector,
                                                              final ProviderContext context) {
            return SpreadsheetComparatorProviders.spreadsheetComparators()
                .spreadsheetComparator(
                    selector,
                    context
                );
        }

        @Override
        public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                              final List<?> values,
                                                              final ProviderContext context) {
            return SpreadsheetComparatorProviders.spreadsheetComparators()
                .spreadsheetComparator(
                    name,
                    values,
                    context
                );
        }

        @Override
        public SpreadsheetComparatorInfoSet spreadsheetComparatorInfos() {
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
            Lists.empty(),
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
