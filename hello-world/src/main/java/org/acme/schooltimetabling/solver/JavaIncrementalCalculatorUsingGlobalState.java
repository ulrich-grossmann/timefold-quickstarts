package org.acme.schooltimetabling.solver;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.calculator.IncrementalScoreCalculator;
import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Timetable;

import java.util.HashMap;
import java.util.function.Function;

public enum JavaIncrementalCalculatorUsingGlobalState implements IncrementalScoreCalculator<Timetable, HardSoftScore> {

  TEACHER_CONFLICTS(Lesson::getTeacher),
  ROOM_CONFLICTS(Lesson::getRoom),
  STUDENT_GROUP_CONFLICTS(Lesson::getStudentGroup);

  JavaIncrementalCalculatorUsingGlobalState(Function<Lesson, Object> conflictObjectExtractor){
    this.conflictObjectExtractor =conflictObjectExtractor;
  }

  final Function<Lesson,Object> conflictObjectExtractor;

  final HashMap<Object, int[]> timeslotToObjectCountMap = new HashMap<>();


  public int getTotalCollisionsCount() {
    return totalCollisionsCount;
  }

  int totalCollisionsCount =0;

  public void recalculateBeforeVariableChange(Lesson lesson){
    if(lesson.getTimeslot()==null)
      return;
    // The counter for the extracted conflict object is decreased for the given timeslot.
    var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(conflictObjectExtractor.apply(lesson), t->new int[100]);

    var objectCountInTimeslot = timeslotToObjectCountMap[lesson.getTimeslot().getId()]--;

    // If the counter is larger than 1, a collision existed before that no longer exists.
    // Therefore, we decrement the number of collisions by one.
    if(objectCountInTimeslot>1)
      totalCollisionsCount--;
  }

  public void recalculateAfterVariableChange(Lesson lesson){
    if(lesson.getTimeslot()==null)
      return;
    // The counter for the extracted conflict object is increased for the given timeslot.
    var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(conflictObjectExtractor.apply(lesson), t->new int[100]);
    var objectCountInTimeslot = timeslotToObjectCountMap[lesson.getTimeslot().getId()]++;

    // If the counter is larger than 1, a collision was added by the planning-variable change.
    // So the collision counter is increased.
    if(objectCountInTimeslot>0)
      totalCollisionsCount++;
  }

  public void clear() {
    totalCollisionsCount=0;
    timeslotToObjectCountMap.clear();
  }

  @Override
  public void resetWorkingSolution(Timetable timetable) {
    // would be nice to have
  }

  @Override
  public void beforeEntityAdded(Object o) {
    // would be nice to have
  }

  @Override
  public void afterEntityAdded(Object o) {
    // would be nice to have
  }

  @Override
  public void beforeVariableChanged(Object o, String s) {
    // would be nice to have
  }

  @Override
  public void afterVariableChanged(Object o, String s) {
    // would be nice to have
  }

  @Override
  public void beforeEntityRemoved(Object o) {
    // would be nice to have
  }

  @Override
  public void afterEntityRemoved(Object o) {
    // would be nice to have
  }

  @Override
  public HardSoftScore calculateScore() {
    // would be nice to have
    return HardSoftScore.ofHard( totalCollisionsCount);
  }
}
