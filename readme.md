# Start the app

`mvn clean verify -Pstart-app` - Start the weather service app on http://localhost:8080 by default


#### Available endpoints

`/hello` - healthcheck

`/weather/<location>/current` - respond with the latest available weather information for today date for the argument location

`/weather/<location>/forecast` - respond with at most 5 weather information in the future for a the argument location


# Start stubs (Wiremock)

`mvn clean verify -Pstart-stubs` - Start wiremock on http://localhost:6543 by default

Check if you can access `http://localhost:6543/__admin/`
