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
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProvider;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link ExpressionFunctionProvider} that wraps a {@link PluginProvider}.
 */
final class PluginProviderExpressionFunctionProvider implements ExpressionFunctionProvider {

    static PluginProviderExpressionFunctionProvider with(final PluginProvider pluginProvider) {
        return new PluginProviderExpressionFunctionProvider(
                Objects.requireNonNull(pluginProvider, "pluginProvider")
        );
    }

    private PluginProviderExpressionFunctionProvider(final PluginProvider pluginProvider) {
        this.pluginProvider = pluginProvider;
    }

    @Override
    public Optional<ExpressionFunction<?, ExpressionEvaluationContext>> expressionFunction(final FunctionExpressionName name) {
        Objects.requireNonNull(name, "name");
        return Cast.to(
                this.pluginProvider.plugin(
                        toPluginName(name),
                        ExpressionFunction.class
                )
        );
    }

    private static PluginName toPluginName(final FunctionExpressionName name) {
        return PluginName.with(name.value());
    }

    @Override
    public Set<ExpressionFunctionInfo> expressionFunctionInfos() {
        return Sets.readOnly(
                this.pluginProvider.pluginInfos()
                        .stream()
                        .map(this::toExpressionFunctionInfos)
                        .collect(Collectors.toCollection(Sets::sorted))
        );
    }

    private ExpressionFunctionInfo toExpressionFunctionInfos(final PluginInfo info) {
        return ExpressionFunctionInfo.with(
                info.url(),
                FunctionExpressionName.with(
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
