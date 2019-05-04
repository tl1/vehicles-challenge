Feature: Find Vehicles

  Background:
    Given stops as defined in stops.csv
    And lines as defined in lines.csv
    And times as defined in times.csv
    And delays as defined in delays.csv

  Scenario: Find a vehicle for a given time and X & Y coordinates
    When I ask for vehicles scheduled to arrive at location 1,1 at 10:00:00
    Then the following vehicles are found
      | stop_id | x | y | line_id | line_name | sta      |
      | 0       | 1 | 1 | 0       | M4        | 10:00:00 |

  Scenario: Return the vehicle arriving next at a given stop
    Given the time is 10:00:00
    When I ask for the next 2 vehicles arriving at stop 3
    Then the following vehicles are found
      | stop_id | x | y | line_id | line_name | sta      | eta      | delay |
      | 3       | 2 | 9 | 0       | M4        | 10:07:00 | 10:08:00 | 1     |
      | 3       | 2 | 9 | 1       | 200       | 10:08:00 | 10:10:00 | 2     |

