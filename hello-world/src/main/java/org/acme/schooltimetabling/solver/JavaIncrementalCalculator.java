package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Timeslot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;

public enum JavaIncrementalCalculator  {

  TEACHER_CONFLICTS(Lesson::getTeacher),
  ROOM_CONFLICTS(Lesson::getRoom),
  STUDENT_GROUP_CONFLICTS(Lesson::getStudentGroup);

  JavaIncrementalCalculator(Function<Lesson, Object> conflictObjectExtractor){
    this.conflictObjectExtractor =conflictObjectExtractor;
  }


  public final Function<Lesson,Object> conflictObjectExtractor;

  final HashMap<Object, int[]> timeslotToObjectCountMap = new HashMap<>();


  public int getTotalCollisionsCount() {
    return totalCollisionsCount;
  }

  int totalCollisionsCount =0;
//
//  public void recalculateBeforeVariableChange(Lesson lesson, boolean b){
//    if(lesson.getTimeslot()==null)
//      return;
//    if(log)
//    System.out.println("SHADOW VARIABLE: before "+lesson + " ts: "+lesson.getTimeslot());
//  }

  boolean log = false;

  public void removeObjectFromTimeslot(Object object, Timeslot timeslot){
    if(object==null)
      return;
//    var adding ="Removing";
//    logIt(object,timeslot,adding);

    if(timeslot!=null) {
      var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(object, t -> new int[100]);
     // System.out.println("before removing " +object+ " counts are "+ Arrays.toString(timeslotToObjectCountMap));
      var objectCountInTimeslot = timeslotToObjectCountMap[timeslot.getId()]--;
      //System.out.println("after removing "+object +" counts are "+ Arrays.toString(timeslotToObjectCountMap));
      if (objectCountInTimeslot > 1)
        totalCollisionsCount--;
    }
  }

  private void logIt(Object lesson, Timeslot nextTimeslot, String adding) {
//    if(log) {
      //System.out.println(adding +" for " + lesson +" in timeslot " + nextTimeslot);
//      try {
//        Thread.sleep(100);
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//    }
  }

  public void addObjectToTimeslot(Object object, Timeslot timeslot){
    if(object==null)
      return;
//    var adding ="ADDING";
//   logIt(object,timeslot,adding);
    if(timeslot!=null) {
       var timeslotToObjectCountMap = this.timeslotToObjectCountMap.computeIfAbsent(object, t -> new int[100]);
//      System.out.println("before adding " +object+" counts are "+ Arrays.toString(timeslotToObjectCountMap));
       var objectCountInTimeslot = timeslotToObjectCountMap[timeslot.getId()]++;
//      System.out.println("after adding "+object +" counts are "+ Arrays.toString(timeslotToObjectCountMap));
      if (objectCountInTimeslot > 0)
        totalCollisionsCount++;
    }
  }

//  public void recalculateAfterVariableChange(Lesson lesson, boolean b){
//    if(b)
//      log=true;
//    if(lesson.getTimeslot()==null)
//      return;
//    if(log)
//      System.out.println("SHADOW VARIABLE: after "+lesson + " ts : "+lesson.getTimeslot());
//  }

  public void clear() {
    totalCollisionsCount=0;
    timeslotToObjectCountMap.clear();
  }

}
