package api

import com.google.inject.AbstractModule
import repositories.{CsvVehicleBuilder, InMemoryVehicleRepository}
import vehicles.VehicleRepository

/**
  * Defines API collaborators.
  */
class Module extends AbstractModule {
  def configure() = {
    val vehicles = CsvVehicleBuilder()
      .withTimesCsv(getClass.getResourceAsStream("/times.csv"))
      .withStopsCsv(getClass.getResourceAsStream("/stops.csv"))
      .withLinesCsv(getClass.getResourceAsStream("/lines.csv"))
      .withDelaysCsv(getClass.getResourceAsStream("/delays.csv"))
      .build()

    bind(classOf[VehicleRepository])
      .toInstance(new InMemoryVehicleRepository(vehicles))
  }
}
