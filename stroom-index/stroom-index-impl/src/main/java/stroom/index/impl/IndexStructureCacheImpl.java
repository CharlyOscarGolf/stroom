/*
 * Copyright 2017 Crown Copyright
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

package stroom.index.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import stroom.cache.api.CacheManager;
import stroom.cache.api.CacheUtil;
import stroom.docref.DocRef;
import stroom.index.shared.IndexDoc;
import stroom.index.shared.IndexField;
import stroom.index.shared.IndexFieldsMap;
import stroom.util.shared.Clearable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Singleton
public class IndexStructureCacheImpl implements IndexStructureCache, Clearable {
    private static final int MAX_CACHE_ENTRIES = 100;

    private final LoadingCache<DocRef, IndexStructure> cache;

    @Inject
    @SuppressWarnings("unchecked")
    IndexStructureCacheImpl(final CacheManager cacheManager,
                            final IndexStore indexStore) {
        final CacheLoader<DocRef, IndexStructure> cacheLoader = CacheLoader.from(k -> {
            if (k == null) {
                throw new NullPointerException("Null key supplied");
            }

            final IndexDoc loaded = indexStore.readDocument(k);
            if (loaded == null) {
                throw new NullPointerException("No index can be found for: " + k);
            }

            // Create a map of index fields keyed by name.
            List<IndexField> indexFields = loaded.getFields();
            if (indexFields == null) {
                indexFields = new ArrayList<>();
            }

            final IndexFieldsMap indexFieldsMap = new IndexFieldsMap(indexFields);
            return new IndexStructure(loaded, indexFields, indexFieldsMap);
        });

        final CacheBuilder cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(MAX_CACHE_ENTRIES)
                .expireAfterWrite(10, TimeUnit.MINUTES);
        cache = cacheBuilder.build(cacheLoader);
        cacheManager.registerCache("Index Config Cache", cacheBuilder, cache);
    }

    @Override
    public IndexStructure get(final DocRef key) {
        return cache.getUnchecked(key);
    }

    @Override
    public void remove(final DocRef key) {
        cache.invalidate(key);
    }

    @Override
    public void clear() {
        CacheUtil.clear(cache);
    }
}
