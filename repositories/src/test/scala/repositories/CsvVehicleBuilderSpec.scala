package repositories

import java.io.{ByteArrayInputStream, InputStream}
import java.nio.charset.StandardCharsets
import java.time.LocalTime

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

class CsvVehicleBuilderSpec extends FlatSpec with Matchers {

  private implicit def stringToInputStream(csv: String): InputStream =
    new ByteArrayInputStream(csv.getBytes(StandardCharsets.UTF_8))

  private val times =
    """
      |line_id,stop_id,time
      |0,3,10:00:00
      |1,2,10:03:00
    """.stripMargin

  private val lines =
    """
      |line_id,line_name
      |0,M48
      |1,M29
    """.stripMargin

  private val stops =
    """
      |stop_id,x,y
      |3,4,7
      |2,1,8
    """.stripMargin

  private val delays =
    """
      |line_name,delay
      |M29,4
    """.stripMargin

  "CsvVehicleBuilder" should "build vehicles from 'times.csv'" in {
    val vehicles = CsvVehicleBuilder()
      .withTimesCsv(times)
      .build()
    vehicles should have size 2
    vehicles.head.lineId shouldBe 0
    vehicles.head.stopId shouldBe 3
    vehicles.head.sta shouldBe LocalTime.of(10,0,0)
    vehicles(1).lineId shouldBe 1
    vehicles(1).stopId shouldBe 2
    vehicles(1).sta shouldBe LocalTime.of(10,3,0)
  }

  it should "add line names from 'lines.csv'" in {
    val vehicles = CsvVehicleBuilder()
      .withTimesCsv(times)
      .withLinesCsv(lines)
      .build()
    vehicles should have size 2
    vehicles.head.lineName shouldBe "M48"
    vehicles(1).lineName shouldBe "M29"
  }

  it should "add stop location from 'stop.csv'" in {
    val vehicles = CsvVehicleBuilder()
      .withTimesCsv(times)
      .withLinesCsv(lines)
      .withStopsCsv(stops)
      .build()
    vehicles should have size 2
    vehicles.head.stopX shouldBe 4
    vehicles.head.stopY shouldBe 7
    vehicles(1).stopX shouldBe 1
    vehicles(1).stopY shouldBe 8
  }

  it should "add delay from 'delays.csv'" in {
    val vehicles = CsvVehicleBuilder()
      .withTimesCsv(times)
      .withLinesCsv(lines)
      .withStopsCsv(stops)
      .withDelaysCsv(delays)
      .build()
    vehicles should have size 2
    vehicles.head.eta shouldBe LocalTime.of(10,0,0)
    vehicles.head.delay shouldBe (0 minutes)
    vehicles(1).eta shouldBe LocalTime.of(10,7,0)
    vehicles(1).delay shouldBe (4 minutes)
  }
}
