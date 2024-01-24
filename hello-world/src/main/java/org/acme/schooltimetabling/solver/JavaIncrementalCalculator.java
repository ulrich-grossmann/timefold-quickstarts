package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Timeslot;

import java.util.HashMap;
import java.util.function.Function;

public enum JavaIncrementalCalculator {

  TEACHER_CONFLICTS(Lesson::getTeacher),
  ROOM_CONFLICTS(Lesson::getRoom),
  STUDENT_GROUP_CONFLICTS(Lesson::getStudentGroup);

  JavaIncrementalCalculator(Function<Lesson, Object> conflictObjectExtractor) {
    this.conflictObjectExtractor = conflictObjectExtractor;
  }


  public final Function<Lesson, Object> conflictObjectExtractor;

  final HashMap<Object, int[]> timeslotToObjectCountMap = new HashMap<>();


  public int getTotalCollisionsCount() {
    return totalCollisionsCount;
  }

  int totalCollisionsCount = 0;

  public void removeObjectFromTimeslot(Object object, Timeslot timeslot) {
    if (object == null || timeslot == null)
      return;
    var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(object, t -> new int[100]);
    var objectCountInTimeslot = timeslotToObjectCountMap[timeslot.getId()]--;
    if (objectCountInTimeslot > 1)
      totalCollisionsCount--;
  }


  public void addObjectToTimeslot(Object object, Timeslot timeslot) {
    if (object == null || timeslot == null)
      return;
    var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(object, t -> new int[100]);
    var objectCountInTimeslot = timeslotToObjectCountMap[timeslot.getId()]++;
    if (objectCountInTimeslot > 0)
      totalCollisionsCount++;
  }

  public void clear() {
    totalCollisionsCount = 0;
    timeslotToObjectCountMap.clear();
  }
}
