package com.danrus.pas.core.pipeline;

public record PipelineResult<T>(T result, boolean end) {
    public static <T> PipelineResult<T> next(T result) {
        return new PipelineResult<>(result, false);
    }

    public static PipelineResult<?> next() {
        return new PipelineResult<>(null, false);
    }

    public static <T> PipelineResult<T> end(T result) {
        return new PipelineResult<>(result, true);
    }
}