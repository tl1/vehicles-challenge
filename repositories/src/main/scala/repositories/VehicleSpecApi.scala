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
  }

  private implicit val locationSpecLike: SpecLike[LocationSpec, Vehicle] = (spec: LocationSpec, vehicle: Vehicle) => {
    spec.x == vehicle.stopX && spec.y == vehicle.stopY
  }

  private implicit val stopSpecLike: SpecLike[StopSpec, Vehicle] = (spec: StopSpec, vehicle: Vehicle) => {
    spec.stopId == vehicle.stopId
  }

}

