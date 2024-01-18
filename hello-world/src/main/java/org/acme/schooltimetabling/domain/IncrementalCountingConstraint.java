package org.acme.schooltimetabling.domain;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum IncrementalCountingConstraint {

  TEACHER_CONFLICT("Teacher conflict",Lesson::getTeacher),
  ROOM_CONFLICT("Room conflict",Lesson::getRoom),
  STUDENT_GROUP_CONFLICT("Student group conflict",Lesson::getStudentGroup);

  IncrementalCountingConstraint(String constraintId, Function<Lesson, Object> conflictObjectExtractor){
    this.constraintId =constraintId;
    this.conflictObjectExtractor =conflictObjectExtractor;
  }

  final Function<Lesson,Object> conflictObjectExtractor;

  final String constraintId;

  final HashMap<Object, Map<Timeslot,Integer>> timeslotToObjectCountMap = new HashMap<>();

  private int totalCollisionsCount =0;

  public void beforeVariableChange(Lesson lesson){
    // The counter for the extracted conflict object is decreased for the given timeslot.
    var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(conflictObjectExtractor.apply(lesson), t->new HashMap<>());
    int objectCountInTimeslot = timeslotToObjectCountMap.computeIfAbsent(lesson.getTimeslot(), t->0);
    timeslotToObjectCountMap.put(lesson.getTimeslot(),objectCountInTimeslot-1);

    // If the counter is larger than 1, a collision existed before that no longer exists.
    // Therefore, we decrement the number of collisions by one.
    if(objectCountInTimeslot>1)
      totalCollisionsCount--;
  }

  public void afterVariableChange(Lesson lesson){
    // The counter for the extracted conflict object is increased for the given timeslot.
    var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(conflictObjectExtractor.apply(lesson), t->new HashMap<>());
    int objectCountInTimeslot = timeslotToObjectCountMap.computeIfAbsent(lesson.getTimeslot(), t->0);
    timeslotToObjectCountMap.put(lesson.getTimeslot(),objectCountInTimeslot+1);

    // If the counter is larger than 1, a collision was added by the planning-variable change.
    // So the collision counter is increased.
    if(objectCountInTimeslot>0)
      totalCollisionsCount++;
  }

  public Constraint defineConstraint(ConstraintFactory constraintFactory) {
    // All lessons are grouped together and penalized with the total conflict count.
    return constraintFactory.forEach(Lesson.class).groupBy(t->0)
            .penalize(HardSoftScore.ofHard(1),key-> totalCollisionsCount)
            .asConstraint(constraintId);
  }

}
