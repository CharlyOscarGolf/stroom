package stroom.search.impl.extraction;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import stroom.util.shared.IsConfig;

import javax.inject.Singleton;

@Singleton
public class ExtractionConfig implements IsConfig {
    private static final int DEFAULT_MAX_THREADS = 4;
    private static final int DEFAULT_MAX_THREADS_PER_TASK = 2;

    private int maxThreads = DEFAULT_MAX_THREADS;
    private int maxThreadsPerTask = DEFAULT_MAX_THREADS_PER_TASK;

    @JsonPropertyDescription("The absolute maximum number of threads per node, used to extract search results from streams using a pipeline")
    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(final int maxThreads) {
        this.maxThreads = maxThreads;
    }

    @JsonPropertyDescription("The maximum number of threads per search, per node, used to extract search results from streams using a pipeline")
    public int getMaxThreadsPerTask() {
        return maxThreadsPerTask;
    }

    public void setMaxThreadsPerTask(final int maxThreadsPerTask) {
        this.maxThreadsPerTask = maxThreadsPerTask;
    }

    @Override
    public String toString() {
        return "ExtractionConfig{" +
                "maxThreads=" + maxThreads +
                ", maxThreadsPerTask=" + maxThreadsPerTask +
                '}';
    }
}
