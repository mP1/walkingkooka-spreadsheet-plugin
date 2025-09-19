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
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.*;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderSamplesContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelectorToken;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link PluginProvider} that wraps a {@link SpreadsheetFormatterProvider}. The {@link PluginInfo} are created from
 * the {@link SpreadsheetFormatterProvider#spreadsheetFormatterInfos()}.
 */
final class SpreadsheetFormatterProviderPluginProvider implements SpreadsheetFormatterProvider,
    PluginProvider {
    static SpreadsheetFormatterProviderPluginProvider with(final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                           final PluginProviderName name,
                                                           final AbsoluteUrl url) {
        return new SpreadsheetFormatterProviderPluginProvider(
            Objects.requireNonNull(spreadsheetFormatterProvider, "spreadsheetFormatterProvider"),
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(url, "url")
        );
    }

    private SpreadsheetFormatterProviderPluginProvider(final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                       final PluginProviderName name,
                                                       final AbsoluteUrl url) {
        this.spreadsheetFormatterProvider = spreadsheetFormatterProvider;
        this.name = name;
        this.url = url;
    }

    @Override
    public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterSelector selector,
                                                     final ProviderContext context) {
        return this.spreadsheetFormatterProvider.spreadsheetFormatter(
            selector,
            context
        );
    }

    @Override
    public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterName name,
                                                     final List<?> values,
                                                     final ProviderContext context) {
        return this.spreadsheetFormatterProvider.spreadsheetFormatter(
            name,
            values,
            context
        );
    }

    @Override
    public Optional<SpreadsheetFormatterSelectorToken> spreadsheetFormatterNextToken(final SpreadsheetFormatterSelector selector) {
        return this.spreadsheetFormatterProvider.spreadsheetFormatterNextToken(selector);
    }

    @Override
    public List<SpreadsheetFormatterSample> spreadsheetFormatterSamples(final SpreadsheetFormatterSelector selector,
                                                                        final boolean includeSamples,
                                                                        final SpreadsheetFormatterProviderSamplesContext context) {
        return this.spreadsheetFormatterProvider.spreadsheetFormatterSamples(
            selector,
            includeSamples,
            context
        );
    }

    @Override
    public SpreadsheetFormatterInfoSet spreadsheetFormatterInfos() {
        return this.spreadsheetFormatterProvider.spreadsheetFormatterInfos();
    }

    private final SpreadsheetFormatterProvider spreadsheetFormatterProvider;

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
        return this.spreadsheetFormatterInfos()
            .stream()
            .map(SpreadsheetFormatterProviderPluginProvider::toPlugin)
            .collect(Collectors.toSet());
    }

    private static PluginInfo toPlugin(final SpreadsheetFormatterInfo info) {
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
        return this.spreadsheetFormatterProvider.toString();
    }
}
