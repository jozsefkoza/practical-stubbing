#### Create a stub response for current weather, for London

* Forecasts are provided by https://www.metaweather.com/api/#locationday
* London has a Where-on-Earth ID of `44418`
* Stub should return **HTTP 200** response
* Minimum temperature must be **-273.15**
* Applicable date must be **2233-03-22** (!)


#### Create stub response for current weather, for every place, other than London

* Querying the weather of any locations other than London is still failing
* Add a dummy response with a general request matcher


#### Make stub current weathers available through our application

* Where-on-Earth IDs are queried through https://www.metaweather.com/api/#locationsearch
* We have two options:
  * Return the ID of London for London location and return a dummy location info for any other locations
  * Proxy location queries


# Homework

#### Create stub weather forecast for Szeged

We expect the presentation to be like this:

```json
{
  "location": "SunCity",
  "forecast": [
    {
      "date": {
        "year": ...,
        "month": ...,
        "day": ...
      },
      "temperature": {
        "min": ...,
        "max": ...
      },
      "weatherState": ...,
      "probability": {
        "value": ...,
        "message": "Very likely"
      }
    },
    {
      "date": {
        "year": ...,
        "month": ...,
        "day": ...
      },
      "temperature": {
        "min": ...,
        "max": ...
      },
      "weatherState": ...,
      "probability": {
        "value": ...,
        "message": "Not gonna happen"
      }
    },
    {
      "date": {
        "year": ...,
        "month": ...,
        "day": ...
      },
      "temperature": {
        "min": ...,
        "max": ...
      },
      "weatherState": ...,
      "probability": {
        "value": ...,
        "message": "Possible"
      }
    }
  ]
}
```

###### Requirements:

  * `...` values are up to you, but they must be displayed in the presentation model.
  * Location name must be **SunCity**.
  * **3** days' weather must be forecasted.
  * We would like to display the probability of forecasts to the users in a forthcoming feature. For this, we will use the **probability** field of our presentation model. The messages of each probability must match the above model.
  * ++ create stubs mappings so that every other will still remain functional.

###### Hints:
  * Szeged location is not recognized by MetaWeather.
  * Probability is converted with `com.epam.training.TBD` class from the MeatWather service response.
