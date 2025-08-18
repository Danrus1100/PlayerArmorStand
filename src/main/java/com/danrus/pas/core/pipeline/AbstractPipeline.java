package com.danrus.pas.core.pipeline;

import com.danrus.pas.core.pipeline.exception.PipelineException;
import com.danrus.pas.api.request.PasRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPipeline<T> {
    private static Logger LOGGER;

    private final List<PipelineStage<?, ?>> stages = new ArrayList<>();
    private PipelineContext context;


    public AbstractPipeline(PasRequest request) {
        this.context = new PipelineContext(request);
        LOGGER = LoggerFactory.getLogger(this.getClass().getSimpleName());
    }

    protected void addStage(PipelineStage<?, ?> stage) {
        stages.add(stage);
    }

    @SuppressWarnings("unchecked")
    public T execute() throws PipelineException {
        Object result = null;

        for (PipelineStage stage : stages) {
            try {
                result = stage.process(result != null ? result : context.getRequest(), context);
                if (result != null) {
                    return (T) result;
                }
            } catch (PipelineException e) {
                LOGGER.error("Pipeline stage {} failed: {}", stage.getClass().getSimpleName(), e.getMessage(), e);
                throw e; // Rethrow the exception to stop the pipeline execution
            } catch (ClassCastException e) {
                LOGGER.error("Faild to cast result class {} to {} return class: {}", result.getClass().getSimpleName(), this.getClass().getSimpleName(), e.getMessage(), e);
                throw new PipelineException("Cast result error " + e.getMessage());
            }
            catch (Exception e) {
                LOGGER.error("Unexpected error in pipeline stage {}: {}", stage.getClass().getSimpleName(), e.getMessage(), e);
                throw new PipelineException("Unexpected error in pipeline stage " + stage.getClass().getSimpleName());
            }
        }
        return null;
    }



}
