package repositories

import java.time.LocalTime

import org.scalatest.{FlatSpec, Matchers}
import vehicles._

import scala.concurrent.duration._

class VehicleSpecApplicationSpec extends FlatSpec with Matchers {
  val vehicle = Vehicle(0, "LINE", 0, 0, 0, LocalTime.of(0, 0, 0), LocalTime.of(0, 0, 0), 0 minutes)

  "Specs application" should "match same location" in {
    LocationSpec(1, 1).isSatisfiedBy(vehicle.copy(stopX = 1, stopY = 1)) shouldBe true
  }

  it should "not match different location" in {
    LocationSpec(1, 2).isSatisfiedBy(vehicle.copy(stopX = 1, stopY = 1)) shouldBe false
  }

  it should "match same stop" in {
    StopSpec(1).isSatisfiedBy(vehicle.copy(stopId = 1)) shouldBe true
  }

  it should "not match different stop" in {
    StopSpec(1).isSatisfiedBy(vehicle.copy(stopId = 2)) shouldBe false
  }

  it should "match scheduled arrival at given time" in {
    ScheduledArrivalAtSpec(LocalTime.of(10,0,0)).isSatisfiedBy(vehicle.copy(sta = LocalTime.of(10,0,0))) shouldBe true
  }

  it should "not match scheduled arrival at different time" in {
    ScheduledArrivalAtSpec(LocalTime.of(10,0,0)).isSatisfiedBy(vehicle.copy(sta = LocalTime.of(10,0,1))) shouldBe false
  }

  it should "match estimated arrival at given time" in {
    EstimatedArrivalAtOrAfterSpec(LocalTime.of(10,3,0)).isSatisfiedBy(vehicle.copy(eta = LocalTime.of(10,3,0))) shouldBe true
  }

  it should "not match estimated arrival before given time" in {
    EstimatedArrivalAtOrAfterSpec(LocalTime.of(10,3,0)).isSatisfiedBy(vehicle.copy(eta = LocalTime.of(10,0,0))) shouldBe false
  }

  it should "match estimated arrival after given time" in {
    EstimatedArrivalAtOrAfterSpec(LocalTime.of(10,3,0)).isSatisfiedBy(vehicle.copy(eta = LocalTime.of(10,3,1))) shouldBe true
  }

  it should "match 'and' combination of 2 matching specifications" in {
    AndSpec(
      LocationSpec(1, 1),
      ScheduledArrivalAtSpec(LocalTime.of(10,0,0))
    ).isSatisfiedBy(
      vehicle.copy(stopX = 1, stopY = 1, sta = LocalTime.of(10,0,0))
    ) shouldBe true
  }

  it should "not match 'and' combination with 1 not matching specification" in {
    AndSpec(
      LocationSpec(1, 1),
      ScheduledArrivalAtSpec(LocalTime.of(10,0,0))
    ).isSatisfiedBy(
      vehicle.copy(stopX = 1, stopY = 3, sta = LocalTime.of(10,0,0))
    ) shouldBe false
  }

  it should "match generic spec instance" in {
    EstimatedArrivalAtOrAfterSpec(LocalTime.of(10,3,0))
      .asInstanceOf[Spec]
      .isSatisfiedBy(vehicle.copy(eta = LocalTime.of(10,3,0))) shouldBe true
  }
}
