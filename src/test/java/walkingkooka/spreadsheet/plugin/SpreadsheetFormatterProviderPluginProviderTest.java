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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginProviderName;
import walkingkooka.plugin.PluginProviderTesting;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderSamplesContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderTesting;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelectorToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetFormatterProviderPluginProviderTest implements PluginProviderTesting<SpreadsheetFormatterProviderPluginProvider>,
    SpreadsheetFormatterProviderTesting<SpreadsheetFormatterProviderPluginProvider>,
    SpreadsheetMetadataTesting,
    ToStringTesting<SpreadsheetFormatterProviderPluginProvider> {

    private final static AbsoluteUrl SPREADSHEET_FORMATTER_INFO_URL = Url.parseAbsolute("https://example.com/SpreadsheetFormatterInfo123");
    private final static String SPREADSHEET_FORMATTER_INFO_NAME = "test-456";

    private final static SpreadsheetFormatterInfoSet INFOS = SpreadsheetFormatterInfoSet.EMPTY.concat(
        SpreadsheetFormatterInfo.with(
            SPREADSHEET_FORMATTER_INFO_URL,
            SpreadsheetFormatterName.with(SPREADSHEET_FORMATTER_INFO_NAME)
        )
    );

    private final static SpreadsheetFormatterProvider SPREADSHEET_FORMATTER_PROVIDER = new SpreadsheetFormatterProvider() {

        @Override
        public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterSelector selector,
                                                         final ProviderContext context) {
            return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatter(
                selector,
                context
            );
        }

        @Override
        public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterName name,
                                                         final List<?> values,
                                                         final ProviderContext context) {
            return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatter(
                name,
                values,
                context
            );
        }

        @Override
        public Optional<SpreadsheetFormatterSelectorToken> spreadsheetFormatterNextToken(final SpreadsheetFormatterSelector selector) {
            return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterNextToken(selector);
        }

        @Override
        public List<SpreadsheetFormatterSample> spreadsheetFormatterSamples(final SpreadsheetFormatterSelector selector,
                                                                            final boolean includeSamples,
                                                                            final SpreadsheetFormatterProviderSamplesContext context) {
            return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterSamples(
                selector,
                includeSamples,
                context
            );
        }

        @Override
        public SpreadsheetFormatterInfoSet spreadsheetFormatterInfos() {
            return INFOS;
        }
    };

    private final static PluginProviderName NAME = PluginProviderName.with("Test123");

    private final static AbsoluteUrl URL = Url.parseAbsolute("https://example.com/123");

    @Test
    public void testWithNullSpreadsheetFormatterProviderFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormatterProviderPluginProvider.with(
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
            () -> SpreadsheetFormatterProviderPluginProvider.with(
                SPREADSHEET_FORMATTER_PROVIDER,
                null,
                URL
            )
        );
    }

    @Test
    public void testWithNullUrlFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetFormatterProviderPluginProvider.with(
                SPREADSHEET_FORMATTER_PROVIDER,
                NAME,
                null
            )
        );
    }

    @Test
    public void testSpreadsheetFormatterName() {
        this.spreadsheetFormatterAndCheck(
            this.createPluginProvider(),
            SpreadsheetFormatterSelector.parse("text-format-pattern @@"),
            PROVIDER_CONTEXT,
            SpreadsheetPattern.parseTextFormatPattern("@@")
                .formatter()
        );
    }

    @Test
    public void testSpreadsheetFormatterInfos() {
        this.spreadsheetFormatterInfosAndCheck(
            this.createPluginProvider(),
            SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterInfos()
        );
    }

    @Override
    public SpreadsheetFormatterProviderPluginProvider createSpreadsheetFormatterProvider() {
        return this.createPluginProvider();
    }

    @Test
    public void testPluginInfos() {
        this.pluginInfosAndCheck(
            PluginInfo.with(
                SPREADSHEET_FORMATTER_INFO_URL,
                PluginName.with(SPREADSHEET_FORMATTER_INFO_NAME)
            )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createPluginProvider(),
            SPREADSHEET_FORMATTER_PROVIDER.toString()
        );
    }

    // PluginProvider...................................................................................................

    @Override
    public SpreadsheetFormatterProviderPluginProvider createPluginProvider() {
        return SpreadsheetFormatterProviderPluginProvider.with(
            SPREADSHEET_FORMATTER_PROVIDER,
            NAME,
            URL
        );
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<SpreadsheetFormatterProviderPluginProvider> type() {
        return SpreadsheetFormatterProviderPluginProvider.class;
    }
}
