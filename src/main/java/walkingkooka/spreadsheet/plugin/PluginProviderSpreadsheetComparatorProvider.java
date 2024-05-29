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

import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparator;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link SpreadsheetComparatorProvider} that wraps a {@link PluginProvider}.
 */
final class PluginProviderSpreadsheetComparatorProvider implements SpreadsheetComparatorProvider {

    static PluginProviderSpreadsheetComparatorProvider with(final PluginProvider pluginProvider) {
        return new PluginProviderSpreadsheetComparatorProvider(
                Objects.requireNonNull(pluginProvider, "pluginProvider")
        );
    }

    private PluginProviderSpreadsheetComparatorProvider(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    @Override
    public Optional<SpreadsheetComparator<?>> spreadsheetComparator(final SpreadsheetComparatorName name) {
        Objects.requireNonNull(name, "name");
        return Cast.to(
                this.pluginProvider.plugin(
                        toPluginName(name),
                        SpreadsheetComparator.class
                )
        );
    }

    private static PluginName toPluginName(final SpreadsheetComparatorName name) {
        return PluginName.with(name.value());
    }

    @Override
    public Set<SpreadsheetComparatorInfo> spreadsheetComparatorInfos() {
        return Sets.readOnly(
                this.pluginProvider.pluginInfos()
                        .stream()
                        .map(this::toSpreadsheetComparatorInfos)
                        .collect(Collectors.toCollection(Sets::sorted))
        );
    }

    private SpreadsheetComparatorInfo toSpreadsheetComparatorInfos(final PluginInfo info) {
        return SpreadsheetComparatorInfo.with(
                info.url(),
                SpreadsheetComparatorName.with(
                        info.name()
                                .value()
                )
        );
    }

    private final PluginProvider pluginProvider;

    // ToString.........................................................................................................

    @Override
    public String toString() {
        return this.pluginProvider.toString();
    }
}
