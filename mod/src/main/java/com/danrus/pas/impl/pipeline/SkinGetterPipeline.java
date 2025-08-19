package com.danrus.pas.impl.pipeline;

import com.danrus.pas.api.data.PasSkin;
import com.danrus.pas.api.request.PasRequest;
import com.danrus.pas.api.request.result.Promise;
import com.danrus.pas.core.pipeline.AbstractPipeline;

public class SkinGetterPipeline extends AbstractPipeline<Promise<PasSkin>> {
    public SkinGetterPipeline(PasRequest request) {
        super(request);
    }
}
