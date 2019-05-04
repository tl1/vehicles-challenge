package repositories

import java.time.LocalTime

import org.scalatest.{FlatSpec, Matchers}
import vehicles._

import scala.concurrent.duration._

class VehicleSortingSpec extends FlatSpec with Matchers {

  private val vehicles = (1 to 5).map(_ => Vehicle(
    0, "LINE", 0, 0, 0,
    LocalTime.of((Math.random() * 23).toInt, 0, 0),
    LocalTime.of((Math.random() * 23).toInt, 0, 0),
    0 minutes
  ))

  "Vehicle sorting" should "sort by estimated arrival" in {
    vehicles.sortWith(SortByScheduledArrival).map(_.sta) shouldBe sorted
  }

}
