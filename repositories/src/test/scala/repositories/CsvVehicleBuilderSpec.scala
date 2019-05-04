package repositories

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import java.time.LocalTime

import org.scalatest.{FlatSpec, Matchers}

class CsvVehicleBuilderSpec extends FlatSpec with Matchers {

  private implicit def stringToInputStream(csv: String): InputStream =
    new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))

  private val times =
    """
      |line_id,stop_id,time
      |0,3,10:00:00
      |1,2,10:03:00
    """.stripMargin

  "CsvVehicleBuilder" should "build vehicles from 'times.csv'" in {
    val vehicles = CsvVehicleBuilder().withTimesCsv(times).build()
    vehicles should have size 2
    vehicles.head.lineId shouldBe 0
    vehicles.head.stopId shouldBe 3
    vehicles.head.sta shouldBe LocalTime.of(10,0,0)
    vehicles(1).lineId shouldBe 1
    vehicles(1).stopId shouldBe 2
    vehicles(1).sta shouldBe LocalTime.of(10,3,0)
  }
}
