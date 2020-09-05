package io.github.sokrato.mw;

public class DefaultEngine<Q, R> implements Engine<Q, R> {
    private final PipelineInitializer<Q, R> pipelineInitializer;

    public DefaultEngine(PipelineInitializer<Q, R> pipelineInitializer) {
        this.pipelineInitializer = pipelineInitializer;
    }

    @Override
    public R process(Q req) {
        Pipeline<Q, R> pipeline = new Pipeline<>();
        return pipelineInitializer.init(pipeline).process(req);
    }
}
