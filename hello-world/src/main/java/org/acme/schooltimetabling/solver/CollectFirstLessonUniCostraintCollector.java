package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.score.stream.uni.UniConstraintCollector;
import org.acme.schooltimetabling.domain.Lesson;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

class CollectFirstLessonUniCostraintCollector implements UniConstraintCollector<Lesson, Lesson[], Lesson> {
  private final AtomicBoolean skip = new AtomicBoolean();
  private final Runnable emptyUndoAccumulator = () -> {
    // undo accumulator left empty...
  };

  @Override
  public Supplier<Lesson[]> supplier() {
    return () -> {
      skip.set(false);
      return new Lesson[1];
    };
  }

  @Override
  public BiFunction<Lesson[], Lesson, Runnable> accumulator() {
    return (accumulated, evaluated) -> {
      if (!skip.get() && evaluated != null) {
        accumulated[0] = evaluated;
        skip.set(true);
      }
      return emptyUndoAccumulator;
    };
  }

  @Override
  public Function<Lesson[], Lesson> finisher() {
    return t -> t[0];
  }
}
