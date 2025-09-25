package com.danrus.pas.impl.pipeline.skin.stages;

import com.danrus.pas.api.request.PasRequest;
import com.danrus.pas.core.pipeline.PasPipelineContext;
import com.danrus.pas.core.pipeline.PasPipelineStage;
import com.danrus.pas.core.pipeline.PipelineResult;
import com.danrus.pas.core.pipeline.exception.PipelineException;
import com.danrus.pas.impl.lazy.PasSkinResult;
import com.danrus.pas.utils.FileSystemHelper;
import com.danrus.pas.utils.VersioningUtils;

import java.io.File;
import java.nio.file.Path;

public class LocalStorageStage implements PasPipelineStage<PasRequest, PasSkinResult> {

    @Override
    public PipelineResult<PasSkinResult> process(PasRequest input, PasPipelineContext context) throws PipelineException {
        return tryFindInCacheStorage(input, context);
    }

    public PipelineResult<PasSkinResult> tryFindInCacheStorage(PasRequest request, PasPipelineContext context) {
//        String filename = ""; // Dummy
//        Path skin = FileSystemHelper.getCachedSkinPath(filename);
//        if (!filename.isEmpty()){
//            return PipelineResult.end(new PasSkinResult(skin, filename, context.getMetadata("slim", Boolean.class)));
//        }
//        return null;
    }
}
