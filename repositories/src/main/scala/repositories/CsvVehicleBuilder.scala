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
  linesCsv: Option[CsvReader[ReadResult[Line]]] = None
) {

  /**
    * Sets times CSV for this builder.
    *
    * @param times Stream that provides times as CSV.
    * @return This builder.
    */
  def withTimesCsv(times: InputStream): CsvVehicleBuilder =
    copy(timesCsv = Some(times.asCsvReader[Time](rfc.withHeader)))

  def withLinesCsv(lines: InputStream): CsvVehicleBuilder =
    copy(linesCsv = Some(lines.asCsvReader[Line](rfc.withHeader)))

  /**
    * Builds vehicles from provided CSV sources.
    * @return Vehicles.
    */
  def build(): Seq[Vehicle] = {
    val lines = linesCsv
      .map(_.toSeq.filter(_.isRight).map(_.right.get).map(l => l.lineId -> l).toMap)
      .getOrElse(Map.empty)
    
    val vehicles = timesCsv
      .map(_.toSeq.filter(_.isRight).map(_.right.get).map(time => Vehicle(
        time.lineId,
        lines.get(time.lineId).map(_.lineName).getOrElse(""),
        time.stopId,
        0, 0,
        time.time,
        LocalTime.now(),
        0 minutes
      )))
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
/** Represents an entry from 'delays.csv'. */
case class Delay(lineName: String, delay: Duration)
