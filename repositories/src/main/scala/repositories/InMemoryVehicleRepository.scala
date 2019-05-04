package repositories

import vehicles._

/**
  * Simple in memory implementation of [[VehicleRepository]].
  */
class InMemoryVehicleRepository extends VehicleRepository {
  override def get(query: Query): List[Vehicle] = ???
}
