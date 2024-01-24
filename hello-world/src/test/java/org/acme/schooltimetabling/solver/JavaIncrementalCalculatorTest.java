package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.Timeslot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JavaIncrementalCalculatorTest {

  @Test
  void recalculateTimeslot() {
    var lesson =new Lesson();
    var timeslot = new Timeslot();
    JavaIncrementalCalculator.TEACHER_CONFLICTS.addObjectToTimeslot(lesson, timeslot);
    JavaIncrementalCalculator.TEACHER_CONFLICTS.addObjectToTimeslot(lesson, timeslot);
    Assertions.assertEquals(0,JavaIncrementalCalculator.TEACHER_CONFLICTS.totalCollisionsCount);
  }
}