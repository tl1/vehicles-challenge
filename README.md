```
curl -X POST \
 -d '@queries/find-a-vehicle-for-a-given-time-and-x-y-coordinates.json' \
 -H 'Content-Type: application/json' \
 'http://localhost:8081/vehicles'
```

```
curl -X POST \
 -d '@queries/return-the-vehicle-arriving-next-at-a-given-stop.json' \
 -H 'Content-Type: application/json' \
 'http://localhost:8081/vehicles'
```
