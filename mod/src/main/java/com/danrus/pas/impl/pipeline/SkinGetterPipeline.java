package com.danrus.pas.impl.pipeline;

import com.danrus.pas.api.PasFeature;
import com.danrus.pas.api.data.PasSkin;
import com.danrus.pas.api.request.PasRequest;
import com.danrus.pas.core.pipeline.AbstractPasPipeline;
import com.danrus.pas.core.pipeline.PasPipelineContext;
import com.danrus.pas.core.pipeline.exception.PipelineException;
import com.danrus.pas.impl.promise.PasSkinResult;

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
        // this.addStage(new LocalCacheStage());
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
