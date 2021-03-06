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

package stroom.core.document;

import stroom.docref.DocRef;
import stroom.docstore.api.DocumentActionHandler;
import stroom.event.logging.api.DocumentEventLog;
import stroom.util.shared.EntityServiceException;

import javax.inject.Inject;

class DocumentServiceImpl implements DocumentService {
    private final DocumentEventLog documentEventLog;
    private final DocumentActionHandlerRegistry documentActionHandlerRegistry;

    @Inject
    public DocumentServiceImpl(final DocumentEventLog documentEventLog, final DocumentActionHandlerRegistry documentActionHandlerRegistry) {
        this.documentEventLog = documentEventLog;
        this.documentActionHandlerRegistry = documentActionHandlerRegistry;
    }

    @Override
    public Object readDocument(DocRef docRef) {
        Object result = null;
        try {
            final DocumentActionHandler documentActionHandler = getDocumentActionHandler(docRef.getType());
            result = documentActionHandler.readDocument(docRef);
            if (result != null) {
                documentEventLog.view(result, null);
            }
        } catch (final RuntimeException e) {
            documentEventLog.view(docRef, e);
            throw e;
        }

        return result;
    }

    @Override
    public Object writeDocument(DocRef docRef, final Object document) {
        Object result = null;
        try {
            final DocumentActionHandler documentActionHandler = getDocumentActionHandler(docRef.getType());
            result = documentActionHandler.writeDocument(document);
            if (result != null) {
                documentEventLog.update(document, result, null);
            }
        } catch (final RuntimeException e) {
            documentEventLog.update(document, result, e);
            throw e;
        }

        return result;
    }

    private DocumentActionHandler getDocumentActionHandler(final String type) {
        final DocumentActionHandler documentActionHandler = documentActionHandlerRegistry.getHandler(type);
        if (documentActionHandler == null) {
            throw new EntityServiceException("No document action handler can be found for type '" + type + "'");
        }
        return documentActionHandler;
    }
}
