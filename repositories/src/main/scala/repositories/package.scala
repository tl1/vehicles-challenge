import vehicles.{SortByScheduledArrival, _}

package object repositories {

  // Specifications ----------------------------------------------------------------------------------------------------

  /**
    * Type class for matching specifications against a subject. Specification design pattern implemented with
    * type class.
    *
    * @tparam A Type of the specification.
    * @tparam S Type of the subject to match against the specification.
    */
  trait SpecLike[A, S] {
    def isSatisfiedBy(spec: A, subject: S): Boolean
  }

  /**
    * Syntax for specification type class.
    */
  implicit class SpecLikeOps[A, S](spec: A) {
    def isSatisfiedBy(subject: S)(implicit specLike: SpecLike[A, S]): Boolean = {
      specLike.isSatisfiedBy(spec, subject)
    }
  }

  implicit val specLike: SpecLike[Spec, Vehicle] = {
    case (location: LocationSpec, vehicle: Vehicle) => location.isSatisfiedBy(vehicle)
    case (stop: StopSpec, vehicle: Vehicle) => stop.isSatisfiedBy(vehicle)
    case (scheduledArrival: ScheduledArrivalAtSpec, vehicle: Vehicle) => scheduledArrival.isSatisfiedBy(vehicle)
    case (estimatedArrival: EstimatedArrivalAtOrAfterSpec, vehicle: Vehicle) => estimatedArrival.isSatisfiedBy(vehicle)
    case (and: AndSpec, vehicle: Vehicle) => and.isSatisfiedBy(vehicle)
  }

  implicit val locationSpecLike: SpecLike[LocationSpec, Vehicle] =
    (spec: LocationSpec, vehicle: Vehicle) => spec.x == vehicle.stopX && spec.y == vehicle.stopY

  implicit val stopSpecLike: SpecLike[StopSpec, Vehicle] =
    (spec: StopSpec, vehicle: Vehicle) => spec.stopId == vehicle.stopId

  implicit val scheduledArrivalAtSpecLike: SpecLike[ScheduledArrivalAtSpec, Vehicle] =
    (spec: ScheduledArrivalAtSpec, vehicle: Vehicle) => spec.sta == vehicle.sta

  implicit val estimatedArrivalAtOrAfterSpecLike: SpecLike[EstimatedArrivalAtOrAfterSpec, Vehicle] =
    (spec: EstimatedArrivalAtOrAfterSpec, vehicle: Vehicle) => spec.eta.isBefore(vehicle.eta) || spec.eta == vehicle.eta

  implicit val andSpecLike: SpecLike[AndSpec, Vehicle] =
    (spec: AndSpec, vehicle: Vehicle) => spec.a.isSatisfiedBy(vehicle) && spec.b.isSatisfiedBy(vehicle)

  // Sorting -----------------------------------------------------------------------------------------------------------

  implicit def sortToLt[S <: Sort](a: S): (Vehicle, Vehicle) => Boolean = a match {
    case SortByScheduledArrival => (v1, v2) => v1.sta.isBefore(v2.sta)
    case SortByEstimatedArrival => (v1, v2) => v1.eta.isBefore(v2.eta)
  }
}
