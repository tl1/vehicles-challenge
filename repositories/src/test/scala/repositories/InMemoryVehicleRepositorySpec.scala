package repositories

import java.time.LocalTime

import org.scalatest._
import Inspectors._
import vehicles._

import scala.concurrent.duration._

class InMemoryVehicleRepositorySpec extends FlatSpec with Matchers {

  private val vehicles = (1 to 10).map(ix => Vehicle(
    ix, "LINE", ix % 3, ix % 5, ix % 2, LocalTime.of(ix % 3, 0, 0), LocalTime.of(ix % 4, 0, 0), 0 minutes
  ))
  private val repo = new InMemoryVehicleRepository(vehicles)

  "InMemoryVehicleRepository" should "only find vehicles matching query" in {
    val found = repo.get(Query(
      StopSpec(2),
      SortByEstimatedArrival,
      None
    ))
    found should have size 3
    forAll (found) { _.stopId shouldBe 2 }
  }

  it should "sort result as requested" in {
    val found = repo.get(Query(
      StopSpec(2),
      SortByEstimatedArrival,
      None
    ))
    found should have size 3
    found.map(_.eta) shouldBe sorted
  }

  it should "apply result limit" in {
    val found = repo.get(Query(
      StopSpec(2),
      SortByEstimatedArrival,
      Some(1)
    ))
    found should have size 1
    found.head.lineId shouldBe vehicles.sortWith(SortByEstimatedArrival).collectFirst {
      case v if v.stopId == 2 => v
    }.get.lineId
  }
}
