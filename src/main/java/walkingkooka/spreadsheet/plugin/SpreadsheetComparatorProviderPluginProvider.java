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
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginProvider;
import walkingkooka.plugin.PluginProviderName;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorSelector;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link PluginProvider} that wraps a {@link SpreadsheetComparatorProvider}. The {@link PluginInfo} are created from
 * the {@link SpreadsheetComparatorProvider#spreadsheetComparatorInfos()}.
 */
final class SpreadsheetComparatorProviderPluginProvider implements SpreadsheetComparatorProvider,
    PluginProvider {

    static SpreadsheetComparatorProviderPluginProvider with(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                            final PluginProviderName name,
                                                            final AbsoluteUrl url) {
        return new SpreadsheetComparatorProviderPluginProvider(
            Objects.requireNonNull(spreadsheetComparatorProvider, "spreadsheetComparatorProvider"),
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(url, "url")
        );
    }

    private SpreadsheetComparatorProviderPluginProvider(final SpreadsheetComparatorProvider spreadsheetComparatorProvider,
                                                        final PluginProviderName name,
                                                        final AbsoluteUrl url) {
        this.spreadsheetComparatorProvider = spreadsheetComparatorProvider;
        this.name = name;
        this.url = url;
    }

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorSelector selector,
                                                          final ProviderContext context) {
        return this.spreadsheetComparatorProvider.spreadsheetComparator(
            selector,
            context
        );
    }

    @Override
    public SpreadsheetComparator<?> spreadsheetComparator(final SpreadsheetComparatorName name,
                                                          final List<?> values,
                                                          final ProviderContext context) {
        return this.spreadsheetComparatorProvider.spreadsheetComparator(
            name,
            values,
            context
        );
    }

    @Override
    public SpreadsheetComparatorInfoSet spreadsheetComparatorInfos() {
        return this.spreadsheetComparatorProvider.spreadsheetComparatorInfos();
    }

    private final SpreadsheetComparatorProvider spreadsheetComparatorProvider;

    // PluginProvider...................................................................................................

    @Override
    public PluginProviderName name() {
        return this.name;
    }

    private final PluginProviderName name;

    @Override
    public AbsoluteUrl url() {
        return this.url;
    }

    private final AbsoluteUrl url;

    @Override
    public Set<PluginInfo> pluginInfos() {
        return this.spreadsheetComparatorInfos()
            .stream()
            .map(SpreadsheetComparatorProviderPluginProvider::toPlugin)
            .collect(Collectors.toSet());
    }

    private static PluginInfo toPlugin(final SpreadsheetComparatorInfo info) {
        return PluginInfo.with(
            info.url(),
            PluginName.with(
                info.name()
                    .value()
            )
        );
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.spreadsheetComparatorProvider.toString();
    }
}
