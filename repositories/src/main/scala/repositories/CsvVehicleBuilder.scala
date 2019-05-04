package repositories

import java.io.InputStream
import java.time.LocalTime

import kantan.csv.DecodeError.TypeError
import vehicles.Vehicle

import scala.concurrent.duration.Duration
import kantan.csv._
import kantan.csv.ops._

import scala.util.Try
import scala.concurrent.duration._

/**
  * Builds a collection of [[Vehicle]] from the provided data files.
  */
case class CsvVehicleBuilder(
  timesCsv: Option[CsvReader[ReadResult[Time]]] = None,
  linesCsv: Option[CsvReader[ReadResult[Line]]] = None,
  stopsCsv: Option[CsvReader[ReadResult[Stop]]] = None,
  delaysCsv: Option[CsvReader[ReadResult[Delay]]] = None
) {

  /**
    * Sets times CSV for this builder.
    *
    * @param times Stream that provides times as CSV.
    * @return This builder.
    */
  def withTimesCsv(times: InputStream): CsvVehicleBuilder =
    copy(timesCsv = Some(times.asCsvReader[Time](rfc.withHeader)))

  /**
    * Sets lines CSV for this builder.
    *
    * @param lines Stream that provides lines as CSV.
    * @return This builder.
    */
  def withLinesCsv(lines: InputStream): CsvVehicleBuilder =
    copy(linesCsv = Some(lines.asCsvReader[Line](rfc.withHeader)))

  /**
    * Sets stops CSV for this builder.
    *
    * @param stops Stream that provides stops as CSV.
    * @return This builder.
    */
  def withStopsCsv(stops: InputStream): CsvVehicleBuilder =
    copy(stopsCsv = Some(stops.asCsvReader[Stop](rfc.withHeader)))

  /**
    * Sets delays CSV for this builder.
    *
    * @param delays Stream that provides delays as CSV.
    * @return This builder.
    */
  def withDelaysCsv(delays: InputStream): CsvVehicleBuilder =
    copy(delaysCsv = Some(delays.asCsvReader[Delay](rfc.withHeader)))

  /**
    * Builds vehicles from provided CSV sources.
    * @return Vehicles.
    */
  def build(): Seq[Vehicle] = {
    val lines = linesCsv
      .map(_.toSeq.filter(_.isRight).map(_.right.get).map(l => l.lineId -> l).toMap)
      .getOrElse(Map.empty)

    val stops = stopsCsv
      .map(_.toSeq.filter(_.isRight).map(_.right.get).map(s => s.stopId -> s).toMap)
      .getOrElse(Map.empty)

    val delays = delaysCsv
      .map(_.toSeq.filter(_.isRight).map(_.right.get).flatMap { d =>
        val lineId = lines.values.find(_.lineName == d.lineName).map(_.lineId)
        lineId.map(_ -> d)
      }.toMap)
      .getOrElse(Map.empty)

    val vehicles = timesCsv
      .map(_.toSeq.filter(_.isRight).map(_.right.get).map { time =>
        val line = lines.get(time.lineId)
        val stop = stops.get(time.stopId)
        val delay = delays.get(time.lineId).map(d => d.delay).getOrElse(0 minutes)

        Vehicle(
          time.lineId,
          line.map(_.lineName).getOrElse(""),
          time.stopId,
          stop.map(_.x).getOrElse(0),
          stop.map(_.y).getOrElse(0),
          time.time,
          time.time.plusMinutes(delay.toMinutes),
          delay
        )})
      .getOrElse(Seq.empty)

    vehicles
  }

}

object CsvMisc {
  implicit val localTimeDecoder: CellDecoder[LocalTime] = CellDecoder.from { t =>
    Try(LocalTime.parse(t)).toEither.left.map(x => TypeError(x.getMessage))
  }
}

/** Represents an entry from 'times.csv'. */
case class Time(lineId: Int, stopId: Int, time: LocalTime)
object Time {
  import CsvMisc._
  implicit val timeDecoder: RowDecoder[Time] = RowDecoder.ordered { (lineId: Int, stopId: Int, time: LocalTime) =>
    new Time(lineId, stopId, time)
  }
}

/** Represents an entry from 'lines.csv'. */
case class Line(lineId: Int, lineName: String)
object Line {
  implicit val lineDecoder: RowDecoder[Line] = RowDecoder.ordered { (lineId: Int, lineName: String) =>
    new Line(lineId, lineName)
  }
}

/** Represents an entry from 'stops.csv'. */
case class Stop(stopId: Int, x: Int, y: Int)
object Stop {
  implicit val stopDecoder: RowDecoder[Stop] = RowDecoder.ordered { (stopId: Int, x: Int, y: Int) =>
    new Stop(stopId, x, y)
  }
}

/** Represents an entry from 'delays.csv'. */
case class Delay(lineName: String, delay: Duration)
object Delay {
  implicit val delayDecoder: RowDecoder[Delay] = RowDecoder.ordered { (lineName: String, delay: Int) =>
    new Delay(lineName, delay minutes)
  }
}
