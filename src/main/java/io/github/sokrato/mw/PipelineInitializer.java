package io.github.sokrato.mw;

public interface PipelineInitializer<Q, R> {
    Pipeline<Q, R> init(Pipeline<Q, R> pipeline);
}
