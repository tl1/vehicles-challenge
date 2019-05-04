package repositories

import vehicles._

/**
  * Simple in-memory implementation of [[VehicleRepository]].
  */
class InMemoryVehicleRepository(vehicles: Seq[Vehicle]) extends VehicleRepository {
  override def get(query: Query): Seq[Vehicle] = {
    val result = vehicles.filter(query.spec.isSatisfiedBy).sortWith(query.sort)
    query.limit.map(limit => result.take(limit)).getOrElse(result)
  }
}
