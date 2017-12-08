package at.fhj.swengb.apps.calculator

import org.scalatest.WordSpecLike
import java.util
import java.nio.file.{Files, Paths}
import scala.collection.JavaConverters._
//for (l <- new java.util.ArrayList().asScala) { println(l) }


class TimeSheetSpec extends WordSpecLike {
  "Something" should {
    "do something" in {
      assert(true)
    }
    "Second test" in {
      val path = Paths.get("C:\\workspace\\fhj.swengb2017.assignments\\calculator\\timesheet-calculator.adoc")
      val lines = Files.readAllLines(path)
      println(lines.size)
    }
    "Test match" in {
      val expected =
        """== Time expenditure: Calculator assignment
          |
          |[cols="1,1,4", options="header"]
          |.Time expenditure
          ||===
          || Date
          || Hours
          || Description
          |
          || 03.12.2017
          || estimated: 2 h
          || needed: 2.5 h
          || Creating buttons
          |
          |
          ||==="""
    }
  }
}
