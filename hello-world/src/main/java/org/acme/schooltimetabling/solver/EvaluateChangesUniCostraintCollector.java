package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.calculator.IncrementalScoreCalculator;
import ai.timefold.solver.core.api.score.stream.uni.UniConstraintCollector;
import org.acme.schooltimetabling.TimetableApp;
import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Room;
import org.acme.schooltimetabling.domain.Timeslot;
import org.acme.schooltimetabling.domain.Timetable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


public enum EvaluateChangesUniCostraintCollector implements IncrementalScoreCalculator<Timetable, HardSoftScore>, UniConstraintCollector<Lesson, AtomicInteger, Integer> {

  SINGLETON;

  public Timetable timetable;

  Map<Lesson, Timeslot> lastTimeslots = new HashMap<>();
  Map<Lesson, Room> lastRooms = new HashMap<>();

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
      var lastTimeslot = lastTimeslots.computeIfAbsent(lesson,t->null);
      var nextTimeslot = lesson.getTimeslot();
      var lastRoom = lastRooms.computeIfAbsent(lesson,t->null);
      var nextRoom = lesson.getRoom();
//      if(!Objects.equals(lastTimeslot,nextTimeslot)&&!Objects.equals(lastRoom,nextRoom)){
//        System.out.println("timeslot and room changed");
//      } else if(!Objects.equals(lastTimeslot,nextTimeslot)){
//        System.out.println("timeslot changed");
//      }
//      else if(!Objects.equals(lastRoom,nextRoom)){
//        System.out.println("room changed");
//      } else
//        System.out.println("nothing changed");
      addLessonToTimeslot(lesson, nextTimeslot, totalScore);
      lastTimeslots.put(lesson, nextTimeslot);
//      if(Objects.equals(lastTimeslot, nextTimeslot)) {
//        //evaluateRoomChange(lesson, nextRoom, lastRoom, totalScore);
//        lastRooms.put(lesson, nextRoom);
//      }
      return ()->{
        removeLessonFromTimeslot(lesson,nextRoom, nextTimeslot, totalScore);
        lastTimeslots.put(lesson,lastTimeslot);
//        if(Objects.equals(lastTimeslot, nextTimeslot)) {
//         // unEvaluateRoomChange(lesson, nextRoom, lastRoom, totalScore);
//          lastRooms.put(lesson, lastRoom);
//        }
      };
    };
  }

  @Override
  public Function<AtomicInteger, Integer> finisher() {
    return t->{
//      for(var value: JavaIncrementalCalculator.values())
//        for(var entry:value.timeslotToObjectCountMap.entrySet()) {
//          var array = entry.getValue();
//          for(var timeslot:timetable.getTimeslots()){
//            var realCount = 0;
//            for(var lesson :timetable.getLessons()){
//              if(Objects.equals(lesson.getTimeslot(), timeslot)) {
//                if (entry.getKey().equals(lesson.getStudentGroup())) {
//                  realCount++;
//                }
//                if (entry.getKey().equals(lesson.getTeacher())) {
//                  realCount++;
//                }
//                if (entry.getKey().equals(lesson.getRoom())) {
//                  realCount++;
//                }
//              }
//            }
//            var theoreticalCount = array[timeslot.getId()];
//            if(theoreticalCount!=realCount){
//              System.out.println("for timeslot "+timeslot+" the real count "+realCount+" for the entry "+entry.getKey() +" " +
//                      "with value "+Arrays.toString(entry.getValue())+ " is unequal the theoretical "+theoreticalCount);
//              TimetableApp.printTimetable(timetable);
//              throw new NullPointerException();
//            }
//          }
//        }



      return t.get();
    };

  }

  @Override
  public void resetWorkingSolution(Timetable timetable) {
    //log.info("resetting!!!!");
  }

  @Override
  public void beforeEntityAdded(Object o) {

  }

  @Override
  public void afterEntityAdded(Object o) {

  }

  @Override
  public void beforeVariableChanged(Object o, String s) {

  }

  @Override
  public void afterVariableChanged(Object o, String s) {

  }

  @Override
  public void beforeEntityRemoved(Object o) {

  }

  @Override
  public void afterEntityRemoved(Object o) {

  }

  @Override
  public HardSoftScore calculateScore() {
    return null;
  }
}
