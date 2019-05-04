package repositories

import vehicles._

/**
  * Simple in-memory implementation of [[VehicleRepository]].
  */
class InMemoryVehicleRepository(vehicles: Seq[Vehicle]) extends VehicleRepository {
  override def get(query: Query): Seq[Vehicle] =
    vehicles.filter(query.spec.isSatisfiedBy)
}
