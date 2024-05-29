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
import walkingkooka.tree.expression.FunctionExpressionName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctions;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionProviderTesting;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PluginProviderExpressionFunctionProviderTest implements ExpressionFunctionProviderTesting<PluginProviderExpressionFunctionProvider>,
        ToStringTesting<PluginProviderExpressionFunctionProvider> {

    @Test
    public void testWithNullPluginProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> PluginProviderExpressionFunctionProvider.with(null)
        );
    }

    @Test
    public void testExpressionFunction() {
        final ExpressionFunction<?, ?> function = ExpressionFunctions.fake();
        final String name = "function-123";

        this.expressionFunctionAndCheck(
                PluginProviderExpressionFunctionProvider.with(
                        new FakePluginProvider() {
                            @Override
                            public <T> Optional<T> plugin(final PluginName n,
                                                          final Class<T> type) {
                                checkEquals(name, n.value());
                                checkEquals(ExpressionFunction.class, type);
                                return Cast.to(
                                        Optional.of(
                                                function
                                        )
                                );
                            }
                        }
                ),
                FunctionExpressionName.with(name),
                function
        );
    }

    @Test
    public void testExpressionFunctionInfos() {
        final AbsoluteUrl url = Url.parseAbsolute("https://example/com/testExpressionFunctionInfos");
        final String name = "function-123";

        this.expressionFunctionInfosAndCheck(
                PluginProviderExpressionFunctionProvider.with(
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
                ExpressionFunctionInfo.with(
                        url,
                        FunctionExpressionName.with(name)
                )
        );
    }

    @Override // skip/disable test
    public void testExpressionFunctionInfosNotEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PluginProviderExpressionFunctionProvider createExpressionFunctionProvider() {
        return PluginProviderExpressionFunctionProvider.with(
                PluginProviders.fake()
        );
    }

    @Test
    public void testToString() {
        final PluginProvider pluginProvider = PluginProviders.fake();

        this.toStringAndCheck(
                PluginProviderExpressionFunctionProvider.with(pluginProvider),
                pluginProvider.toString()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PluginProviderExpressionFunctionProvider> type() {
        return PluginProviderExpressionFunctionProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
