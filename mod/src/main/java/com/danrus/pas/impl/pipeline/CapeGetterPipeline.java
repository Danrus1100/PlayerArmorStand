package com.danrus.pas.impl.pipeline;

import com.danrus.pas.api.request.PasRequest;
import com.danrus.pas.core.pipeline.AbstractPasPipeline;
import com.danrus.pas.core.pipeline.PasPipelineContext;
import com.danrus.pas.impl.lazy.PasCapeResult;

public class CapeGetterPipeline extends AbstractPasPipeline<PasCapeResult> {
    public CapeGetterPipeline(PasRequest request) {
        super(request);
    }

    public CapeGetterPipeline(PasPipelineContext context) {
        super(context);
    }
}
