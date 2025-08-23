package com.danrus.pas.core.pipeline;

import com.danrus.pas.core.pipeline.exception.PipelineException;

public interface PasPipelineStage<T, R>{
    R process(T input, PasPipelineContext context) throws PipelineException;
}
