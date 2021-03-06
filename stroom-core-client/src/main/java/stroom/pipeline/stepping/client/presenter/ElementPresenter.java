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

package stroom.pipeline.stepping.client.presenter;

import com.google.gwt.core.client.Scheduler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.MyPresenterWidget;
import com.gwtplatform.mvp.client.View;
import stroom.alert.client.event.AlertEvent;
import stroom.dispatch.client.ClientDispatchAsync;
import stroom.docref.DocRef;
import stroom.docref.SharedObject;
import stroom.document.client.event.DirtyEvent;
import stroom.document.client.event.DirtyEvent.DirtyHandler;
import stroom.document.client.event.HasDirtyHandlers;
import stroom.editor.client.presenter.EditorPresenter;
import stroom.entity.shared.DocumentServiceReadAction;
import stroom.entity.shared.DocumentServiceWriteAction;
import stroom.pipeline.shared.PipelineStepAction;
import stroom.pipeline.shared.SteppingFilterSettings;
import stroom.pipeline.shared.data.PipelineElementType;
import stroom.pipeline.stepping.client.event.ShowSteppingFilterSettingsEvent;
import stroom.pipeline.stepping.client.presenter.ElementPresenter.ElementView;
import stroom.util.shared.HasData;
import stroom.util.shared.Indicators;
import stroom.widget.util.client.Future;
import stroom.widget.util.client.FutureImpl;

public class ElementPresenter extends MyPresenterWidget<ElementView> implements HasDirtyHandlers {
    private final ClientDispatchAsync dispatcher;
    private final Provider<EditorPresenter> editorProvider;
    private String elementId;
    private PipelineElementType elementType;
    private DocRef entityRef;
    private DocRef fuzzyEntityRef;
    private PipelineStepAction pipelineStepAction;
    private boolean refreshRequired = true;
    private boolean loaded;
    private boolean dirtyCode;
    private DocRef loadedDoc;
    private HasData hasData;
    private Indicators codeIndicators;
    private EditorPresenter codePresenter;
    private EditorPresenter inputPresenter;
    private EditorPresenter outputPresenter;

    @Inject
    public ElementPresenter(final EventBus eventBus, final ElementView view,
                            final Provider<EditorPresenter> editorProvider, final ClientDispatchAsync dispatcher) {
        super(eventBus, view);
        this.editorProvider = editorProvider;
        this.dispatcher = dispatcher;
    }

    public Future<Boolean> load() {
        final FutureImpl<Boolean> future = new FutureImpl<>();

        if (!loaded) {
            loaded = true;
            boolean loading = false;

            if (elementType != null && elementType.hasRole(PipelineElementType.ROLE_HAS_CODE)) {
                getView().setCodeView(getCodePresenter().getView());

                try {
                    if (fuzzyEntityRef != null && fuzzyEntityRef.getName() != null && fuzzyEntityRef.getName().length() > 0) {
                        loadFuzzyEntityRef(future);
                        loading = true;
                    } else {
                        loadEntityRef(future);
                        loading = true;
                    }
                } catch (final RuntimeException e) {
                    AlertEvent.fireErrorFromException(this, e, null);
                }
            }

            // We only care about seeing input if the element mutates the input
            // some how.
            if (elementType != null && elementType.hasRole(PipelineElementType.ROLE_MUTATOR)) {
                getView().setInputView(getInputPresenter().getView());
            }

            // We always want to see the output of the element.
            getView().setOutputView(getOutputPresenter().getView());

            if (!loading) {
                Scheduler.get().scheduleDeferred(() -> future.setResult(true));
            }
        } else {
            future.setResult(true);
        }

        return future;
    }

    private void loadFuzzyEntityRef(final FutureImpl<Boolean> future) {
//        if (TextConverterDoc.DOCUMENT_TYPE.equals(fuzzyEntityRef.getType())) {
        final DocumentServiceReadAction documentServiceReadAction = new DocumentServiceReadAction(fuzzyEntityRef);
//
//            final FindTextConverterCriteria criteria = new FindTextConverterCriteria();
//            criteria.setName(new StringCriteria(fuzzyEntityRef.getName()));
//            criteria.setSort(FindXSLTCriteria.FIELD_ID);
//            final EntityServiceFindAction<FindTextConverterCriteria, TextConverterDoc> findAction = new EntityServiceFindAction<>(criteria);
        dispatcher.exec(documentServiceReadAction)
                .onSuccess(result -> {
                    if (result != null) {
                        loadedDoc = fuzzyEntityRef;
                        hasData = (HasData) result;
                        dirtyCode = false;
                        read();
                        future.setResult(true);
                    } else {
                        // Try and load by entity ref if there is one.
                        loadEntityRef(future);
                    }
                })
                .onFailure(caught -> future.setResult(false));
//        } else if (XsltDoc.DOCUMENT_TYPE.equals(fuzzyEntityRef.getType())) {
//            final FindXSLTCriteria criteria = new FindXSLTCriteria();
//            criteria.setName(new StringCriteria(fuzzyEntityRef.getName()));
//            criteria.setSort(FindXSLTCriteria.FIELD_ID);
//            final EntityServiceFindAction<FindXSLTCriteria, XsltDoc> findAction = new EntityServiceFindAction<>(criteria);
//            dispatcher.exec(findAction)
//                    .onSuccess(result -> {
//                        if (result != null && result.size() > 0) {
//                            loadedDoc = fuzzyEntityRef;
//                            hasData = result.get(0);
//                            dirtyCode = false;
//                            read();
//                            future.setResult(true);
//                        } else {
//                            // Try and load by entity ref if there is one.
//                            loadEntityRef(future);
//                        }
//                    })
//                    .onFailure(caught -> future.setResult(false));
//        } else {
//            Scheduler.get().scheduleDeferred(() -> future.setResult(true));
//        }
    }

    private void loadEntityRef(final FutureImpl<Boolean> future) {
        if (entityRef != null) {
            dispatcher.exec(new DocumentServiceReadAction<>(entityRef))
                    .onSuccess(result -> {
                        loadedDoc = entityRef;
                        hasData = (HasData) result;
                        dirtyCode = false;
                        read();

                        future.setResult(true);
                    })
                    .onFailure(caught -> future.setResult(false));
        } else {
            Scheduler.get().scheduleDeferred(() -> future.setResult(true));
        }
    }

    public void save() {
        if (loaded && hasData != null && dirtyCode) {
            write();
            dispatcher.exec(new DocumentServiceWriteAction<>(loadedDoc, (SharedObject) hasData)).onSuccess(result -> {
                hasData = (HasData) result;
                dirtyCode = false;
            });
        }
    }

    private void read() {
        if (hasData != null) {
            setCode(hasData.getData(), codeIndicators);
        } else {
            setCode("", codeIndicators);
        }
    }

    private void write() {
        hasData.setData(getCode());
    }

    public String getCode() {
        if (codePresenter == null) {
            return null;
        }
        return codePresenter.getText();
    }

    public void setCode(final String code, final Indicators codeIndicators) {
        if (codePresenter != null) {
            this.codeIndicators = codeIndicators;

            if (!codePresenter.getText().equals(code)) {
                codePresenter.setText(code);
            }

            codePresenter.setIndicators(codeIndicators);
        }
    }

    public void setCodeIndicators(final Indicators codeIndicators) {
        if (codePresenter != null) {
            this.codeIndicators = codeIndicators;
            codePresenter.setIndicators(codeIndicators);
        }
    }

    public void setInput(final String input, final int inputStartLineNo, final boolean formatInput,
                         final Indicators inputIndicators) {
        if (inputPresenter != null) {
            inputPresenter.getStylesOption().setOn(formatInput);

            if (!inputPresenter.getText().equals(input)) {
                inputPresenter.setText(input, formatInput);
            }

            inputPresenter.setFirstLineNumber(inputStartLineNo);
            inputPresenter.setIndicators(inputIndicators);
        }
    }

    public void setOutput(final String output, final int outputStartLineNo, final boolean formatOutput,
                          final Indicators outputIndicators) {
        if (outputPresenter != null) {
            outputPresenter.getStylesOption().setOn(formatOutput);

            if (!outputPresenter.getText().equals(output)) {
                outputPresenter.setText(output, formatOutput);
            }

            outputPresenter.setFirstLineNumber(outputStartLineNo);
            outputPresenter.setIndicators(outputIndicators);
        }
    }

    @Override
    public HandlerRegistration addDirtyHandler(final DirtyHandler handler) {
        return addHandlerToSource(DirtyEvent.getType(), handler);
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(final String elementId) {
        this.elementId = elementId;
    }

    public PipelineElementType getElementType() {
        return elementType;
    }

    public void setElementType(final PipelineElementType elementType) {
        this.elementType = elementType;
    }

    public void setEntityRef(final DocRef entityRef) {
        this.entityRef = entityRef;
    }

    public void setFuzzyEntityRef(final DocRef fuzzyEntityRef) {
        this.fuzzyEntityRef = fuzzyEntityRef;
    }

    public void setPipelineStepAction(final PipelineStepAction pipelineStepAction) {
        this.pipelineStepAction = pipelineStepAction;
    }

    public boolean isRefreshRequired() {
        return refreshRequired;
    }

    public void setRefreshRequired(final boolean refreshRequired) {
        this.refreshRequired = refreshRequired;
    }

    public boolean isDirtyCode() {
        return dirtyCode;
    }

    private EditorPresenter getCodePresenter() {
        if (codePresenter == null) {
            codePresenter = editorProvider.get();
            setOptions(codePresenter);
            codePresenter.getLineNumbersOption().setOn(true);

            registerHandler(codePresenter.addValueChangeHandler(event -> {
                dirtyCode = true;
                DirtyEvent.fire(ElementPresenter.this, true);
            }));
            registerHandler(codePresenter.addFormatHandler(event -> {
                dirtyCode = true;
                DirtyEvent.fire(ElementPresenter.this, true);
            }));
        }
        return codePresenter;
    }

    private EditorPresenter getInputPresenter() {
        if (inputPresenter == null) {
            inputPresenter = editorProvider.get();
            inputPresenter.setReadOnly(true);
            setOptions(inputPresenter);

            inputPresenter.setShowFilterSettings(false);
            inputPresenter.setInput(true);
        }
        return inputPresenter;
    }

    private EditorPresenter getOutputPresenter() {
        if (outputPresenter == null) {
            outputPresenter = editorProvider.get();
            outputPresenter.setReadOnly(true);
            setOptions(outputPresenter);

            // Turn on line numbers for the output presenter if this is a validation step as the output needs to show
            // validation errors in the gutter.
            if (elementType != null && elementType.hasRole(PipelineElementType.ROLE_VALIDATOR)) {
                outputPresenter.getLineNumbersOption().setOn(true);
            }

            outputPresenter.setShowFilterSettings(true);
            outputPresenter.setInput(false);

            registerHandler(outputPresenter.addChangeFilterHandler(event -> {
                final SteppingFilterSettings settings = pipelineStepAction.getStepFilter(elementId);
                ShowSteppingFilterSettingsEvent.fire(ElementPresenter.this, outputPresenter, false, elementId,
                        settings);
            }));
        }
        return outputPresenter;
    }

    private void setOptions(final EditorPresenter editorPresenter) {
        editorPresenter.getIndicatorsOption().setAvailable(true);
        editorPresenter.getIndicatorsOption().setOn(true);
        editorPresenter.getLineNumbersOption().setAvailable(true);
        editorPresenter.getLineNumbersOption().setOn(false);
    }

    public interface ElementView extends View {
        void setCodeView(View view);

        void setInputView(View view);

        void setOutputView(View view);
    }
}