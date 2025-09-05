package com.danrus.pas.impl.pipeline.skin;

import com.danrus.pas.api.feature.PasFeature;
import com.danrus.pas.api.request.PasRequest;
import com.danrus.pas.core.pipeline.AbstractPasPipeline;
import com.danrus.pas.core.pipeline.PasPipelineContext;
import com.danrus.pas.core.pipeline.exception.PipelineException;
import com.danrus.pas.impl.lazy.PasSkinResult;
import com.danrus.pas.impl.pipeline.skin.stages.LocalStorageStage;

public class SkinGetterPipeline extends AbstractPasPipeline<PasSkinResult> {
    public SkinGetterPipeline(PasRequest request) {
        super(request);
        preparePipeline();
    }

    public SkinGetterPipeline(PasPipelineContext context) {
        super(context);
        preparePipeline();
    }

    private void preparePipeline() {
         this.addStage(new LocalStorageStage());
        // this.addStage(new RemoteFetchStage());
        // this.addStage(new DefaultSkinStage());
    }

    private void prepareContext() {
        for (PasFeature feature : context.getRequest().getData().features()) {
            feature.apply(context);
        }
    }

    @Override
    public PasSkinResult execute() throws PipelineException {
        prepareContext();
        return super.execute();
    }
}
