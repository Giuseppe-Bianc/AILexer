package org.dersbian;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode({Mode.AverageTime, Mode.Throughput, Mode.SampleTime})
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 15, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, time = 100, timeUnit = TimeUnit.MILLISECONDS)
public class MyBenchmark {
    private AILexer lexer;

    @Setup
    public void setup() {
        lexer = new AILexer(Costanti.code);
    }


    @Benchmark
    public void becmarkLexer() throws InvalidTokenException {
        lexer.getAllTokens();
    }

    @Benchmark
    public void becmarkLexer2() throws InvalidTokenException {
        AILexer llexer = new AILexer("");
        llexer.getAllTokens();
    }

}
