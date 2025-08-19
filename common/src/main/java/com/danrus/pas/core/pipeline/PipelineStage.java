package com.danrus.pas.core.pipeline;

import com.danrus.pas.core.pipeline.exception.PipelineException;

public interface PipelineStage <T, R>{
    R process(T input, PipelineContext context) throws PipelineException;
}
