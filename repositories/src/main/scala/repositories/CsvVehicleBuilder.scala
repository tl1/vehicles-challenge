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
  times: Option[CsvReader[ReadResult[Time]]] = None
) {

  /**
    * Sets times CSV for this builder.
    *
    * @param times Stream that provides times as CSV.
    * @return This builder.
    */
  def withTimesCsv(times: InputStream): CsvVehicleBuilder =
    copy(times = Some(times.asCsvReader[Time](rfc.withHeader)))

  def build(): Seq[Vehicle] = {
    (for {
      ts <- times
    } yield {
      ts.toSeq.filter(_.isRight).map(_.right.get).map(time => Vehicle(
        time.lineId,
        "",
        time.stopId,
        0, 0,
        time.time,
        LocalTime.now(),
        0 minutes
      ))
    }).getOrElse(Seq.empty)
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
/** Represents an entry from 'stops.csv'. */
case class Stop(stopId: Int, x: Int, y: Int)
/** Represents an entry from 'delays.csv'. */
case class Delay(lineName: String, delay: Duration)
