# Vehicles Challenge

Solution for a coding challenge in the area of public transport.

## Summary

- Prerequisites:
  - Java 8+
  - sbt
- Run test cases: `sbt test`
- Run application in dev mode: `sbt api/run`
- Run queries:
  - Find a vehicle for a given time and X & Y coordinates
    ```
    curl -X POST \
     -d '@queries/find-a-vehicle-for-a-given-time-and-x-y-coordinates.json' \
     -H 'Content-Type: application/json' \
     'http://localhost:8081/vehicles'
    ```
    Looks up vehicles arriving at 1,1 at 10:00:00. Location and time
    can be edited in the query file.
  - Return the vehicle arriving next at a given stop
    ```
    curl -X POST \
     -d '@queries/return-the-vehicle-arriving-next-at-a-given-stop.json' \
     -H 'Content-Type: application/json' \
     'http://localhost:8081/vehicles'
    ```
    Assuming it is 10:00:00, looks up the vehicle arriving next at stop 3.
    Time and stop ID can be changed in the query file, also multiple
    vehicles can be retrieved sorted by their time of arrival.
  - More queries can be constructed in a similar fashion as the 2 example
    queries, the query model allows arbitrary combinations of given
    query parts. I leave it up to the reviewer to experiment, if
    interested.
- Project contents:
  - `/vehicles` Domain model around Vehicle concept.
  - `/api` HTTP API based on Play Framework.
  - `/repositories` In memory data repositories created from provided
    CSV files.
  - `/features` Acceptance tests based on Cucumber. Note: Step
    implementation not finished due to time constraints.
  - `/queries` Example queries to run against the API.

## Domain Model

- The domain model is structured around the single concept of a Vehicle,
  being a fully denormalized join of the data from the provided CSV
  files.
- I. e. a Vehicle represents a vehicle of a certain line arriving at
  a certain stop at a certain time, potentially delayed.
- This model has been chosen for simplicity of query execution, mimicking
  a potential read model in a CQRS design.
- In addition, a rich and extensible query model is provided based on the
  "Specification" design pattern.

## Software Design

- The project mainly applies principles from Ports & Adapters architecture,
  with a light-dependency domain module at the center, and 2 adapters
  for the HTTP API and the data store.

## Thoughts / Potential Improvements

- I was a bit unsure about naming the central domain object "Vehicle",
  as it does not represent a real vehicle very well. However, it plays
  quite well with the requirements given. Also any more realistic
  domain model I had in mind didn't work nicely with the CSV data
  provided and felt a bit overcomplicated. So for the context of this
  challenge, I think it was a good choice.
- When I stopped working on the project as the given time was up,
  I was a bit unhappy to not having at least one of the Cucumber
  specs implemented. I realized that this had to do with the way of how
  I approached the implementation, which was rather module by module,
  starting from the center (domain) and moving to the outside.
  A preferable way would have been to work feature by feature in
  vertical slices, and including the according acceptance test into
  each slice. By that I would probably not have all features
  finished in time, but I would have reached a better quality. In the end
  I think that this would have been the better trade off.
- Finally, I would have liked to polish the JSON representation of
  the queries a bit, which in some places is a bit awkward.

