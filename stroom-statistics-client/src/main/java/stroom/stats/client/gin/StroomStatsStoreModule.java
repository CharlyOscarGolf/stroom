/*
 * Copyright 2016 Crown Copyright
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
 */

package stroom.stats.client.gin;

import stroom.core.client.gin.PluginModule;
import stroom.stats.client.StroomStatsStorePlugin;
import stroom.stats.client.presenter.StroomStatsStoreFieldEditPresenter;
import stroom.stats.client.presenter.StroomStatsStorePresenter;
import stroom.stats.client.presenter.StroomStatsStoreSettingsPresenter;
import stroom.stats.client.view.StroomStatsStoreFieldEditViewImpl;
import stroom.stats.client.view.StroomStatsStoreSettingsViewImpl;

public class StroomStatsStoreModule extends PluginModule {
    @Override
    protected void configure() {

        bindPlugin(StroomStatsStorePlugin.class);

        bind(StroomStatsStorePresenter.class);

        bindPresenterWidget(
                StroomStatsStorePresenter.class,
                StroomStatsStoreSettingsPresenter.StroomStatsStoreSettingsView.class,
                StroomStatsStoreSettingsViewImpl.class);

        bindPresenterWidget(
                StroomStatsStoreFieldEditPresenter.class,
                StroomStatsStoreFieldEditPresenter.StroomStatsStoreFieldEditView.class,
                StroomStatsStoreFieldEditViewImpl.class);
    }
}
