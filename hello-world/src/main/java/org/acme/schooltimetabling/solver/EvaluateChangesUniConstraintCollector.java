package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.score.stream.uni.UniConstraintCollector;
import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Room;
import org.acme.schooltimetabling.domain.Timeslot;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


public class EvaluateChangesUniConstraintCollector implements UniConstraintCollector<Lesson, AtomicInteger, Integer> {

  @Override
  public Supplier<AtomicInteger> supplier() {
    return AtomicInteger::new;
  }

  private void addLessonToTimeslot(Lesson lesson, Timeslot timeslot, AtomicInteger totalScore){
    var result = 0;
    for(var value: JavaIncrementalCalculator.values()){
      value.addObjectToTimeslot(value.conflictObjectExtractor.apply(lesson), timeslot);
      result+=value.getTotalCollisionsCount();
    }
    totalScore.set(result);
  }


  private void removeLessonFromTimeslot(Lesson lesson, Room oldRoom, Timeslot nextTimeslot, AtomicInteger totalScore){
    var result = 0;
    for(var value: JavaIncrementalCalculator.values()){
      if(value==JavaIncrementalCalculator.ROOM_CONFLICTS)
        value.removeObjectFromTimeslot(oldRoom,nextTimeslot);
      else
        value.removeObjectFromTimeslot(value.conflictObjectExtractor.apply(lesson), nextTimeslot);
      result+=value.getTotalCollisionsCount();
    }
    totalScore.set(result);
  }

  @Override
  public BiFunction<AtomicInteger, Lesson, Runnable> accumulator() {
    return (totalScore, lesson) -> {
      addLessonToTimeslot(lesson, lesson.getTimeslot(), totalScore);
      var originalRoom = lesson.getRoom();
      var originalTimeslot = lesson.getTimeslot();
      return ()-> removeLessonFromTimeslot(lesson,originalRoom, originalTimeslot, totalScore);
    };
  }

  @Override
  public Function<AtomicInteger, Integer> finisher() {
    return AtomicInteger::get;
  }
}
