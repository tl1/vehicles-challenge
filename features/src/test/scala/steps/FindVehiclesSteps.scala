package steps

import cucumber.api.scala.{EN, ScalaDsl}

class FindVehiclesSteps extends ScalaDsl with EN {
  Given("""[^ ]+ as defined in .*"""){ () =>
    // Fixture handling not implemented
  }

  Given("""the time is (\d{2}:\d{2}:\d{2})""") { time: String =>

  }

  When("""I ask for vehicles scheduled to arrive at location (\d+),(\d+) at (\d{2}:\d{2}:\d{2})"""){ (x: String, y: String, time: String) =>

  }

  When("""I ask for the next (\d+) vehicles arriving at stop (\d+)"""){ (count: String, stopId: String) =>

  }

  Then("""the following vehicles are found"""){ vehicles: Any =>

  }
}