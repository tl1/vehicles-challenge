package repositories

import vehicles._

/**
  * Type class for interpreting specifications against a subject. Specification design pattern implemented with
  * type class.
  *
  * @tparam A Type of the specification.
  * @tparam S Type of the subject to check against the specification.
  */
trait SpecLike[A, S] {
  def isSatisfiedBy(spec: A, subject: S): Boolean
}

/**
  * Syntax for specification type class.
  */
object SpecLikeSyntax {
  implicit class SpecLikeOps[A, S](spec: A) {
    def isSatisfiedBy(subject: S)(implicit specLike: SpecLike[A, S]): Boolean = {
      specLike.isSatisfiedBy(spec, subject)
    }
  }
}

/**
  * Api to execute vehicle specifications against vehicles.
  */
object VehicleSpecApi {
  import SpecLikeSyntax._

  implicit def specLike[S <: Spec]: SpecLike[S, Vehicle] = {
    case (location: LocationSpec, vehicle: Vehicle) => location.isSatisfiedBy(vehicle)
    case (stop: StopSpec, vehicle: Vehicle) => stop.isSatisfiedBy(vehicle)
    case (scheduledArrival: ScheduledArrivalAtSpec, vehicle: Vehicle) => scheduledArrival.isSatisfiedBy(vehicle)
    case (estimatedArrival: EstimatedArrivalAtOrAfterSpec, vehicle: Vehicle) => estimatedArrival.isSatisfiedBy(vehicle)
  }

  private implicit val locationSpecLike: SpecLike[LocationSpec, Vehicle] =
    (spec: LocationSpec, vehicle: Vehicle) => spec.x == vehicle.stopX && spec.y == vehicle.stopY

  private implicit val stopSpecLike: SpecLike[StopSpec, Vehicle] =
    (spec: StopSpec, vehicle: Vehicle) => spec.stopId == vehicle.stopId

  private implicit val scheduledArrivalAtSpecLike: SpecLike[ScheduledArrivalAtSpec, Vehicle] =
    (spec: ScheduledArrivalAtSpec, vehicle: Vehicle) => spec.sta == vehicle.sta

  private implicit val estimatedArrivalAtOrAfterSpecLike: SpecLike[EstimatedArrivalAtOrAfterSpec, Vehicle] =
    (spec: EstimatedArrivalAtOrAfterSpec, vehicle: Vehicle) => spec.eta.isBefore(vehicle.eta) || spec.eta == vehicle.eta
}

