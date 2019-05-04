import java.time.LocalTime

import scala.concurrent.duration.Duration

package object vehicles {

  /**
    * Represents a vehicle arriving at a certain stop at a given time.
    * Fully denormalized data structure for easy querying.
    */
  final case class Vehicle(
    lineId: Int,
    lineName: String,
    stopId: Int,
    stopX: Int,
    stopY: Int,
    sta: LocalTime,
    eta: LocalTime,
    delay: Duration
  )

  /** Specifications for constructing vehicle queries. */
  sealed trait Spec
  /** Specifies vehicles by location. */
  final case class LocationSpec(x: Int, y: Int) extends Spec
  /** Specifies vehicles by stop. */
  final case class StopSpec(stopId: Int) extends Spec
  /** Specifies vehicles by exact scheduled time of arrival. */
  final case class ScheduledArrivalAtSpec(sta: LocalTime) extends Spec
  /** Specifies vehicles by estimated time of arrival at or after a given time. */
  final case class EstimatedArrivalAtOrAfterSpec(eta: LocalTime) extends Spec
  /** Combines two specifications by logical AND. */
  final case class AndSpec(a: Spec, b: Spec) extends Spec

  /** Sortings for constructing vehicle queries. */
  sealed trait Sort
  /** Sorts vehicles by scheduled time of arrival. */
  final case object SortByScheduledArrival extends Sort
  /** Sorts vehicles by estimated time of arrival. */
  final case object SortByEstimatedArrival extends Sort

  /** Full vehicle query consisting of specification, sorting and result size limit. */
  final case class Query(spec: Spec, sort: Sort, limit: Option[Int])

  /**
    * Repository port to retrieve vehicles from storage.
    */
  trait VehicleRepository {
    def get(query: Query): Seq[Vehicle]
  }

}
