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

import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginProvider;
import walkingkooka.plugin.PluginProviderName;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;

/**
 * Factory methods to create a {@link PluginProvider} for some spreadsheet providers.
 */
public final class SpreadsheetPluginProviders implements PublicStaticHelper {

    /**
     * {@see SpreadsheetComparatorProviderPluginProvider}
     */
    public static PluginProvider spreadsheetComparatorProvider(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                               final PluginProviderName name,
                                                               final AbsoluteUrl url) {
        return SpreadsheetComparatorProviderPluginProvider.with(
            spreadsheetComparatorProvider,
            name,
            url
        );
    }

    /**
     * {@see SpreadsheetFormatterProviderPluginProvider}
     */
    public static PluginProvider spreadsheetFormatterProvider(final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                              final PluginProviderName name,
                                                              final AbsoluteUrl url) {
        return SpreadsheetFormatterProviderPluginProvider.with(
            spreadsheetFormatterProvider,
            name,
            url
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetPluginProviders() {
        throw new UnsupportedOperationException();
    }
}
