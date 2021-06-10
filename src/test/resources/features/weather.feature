Feature: Weather API feature
  End point to fetch weather information from weather service

  @development
  Scenario Outline: Get weather for <city_name>
    Given The dependencies are up and running
    And Response from Weather service is set up for <city_name>
    When the user fetches the weather information for <city_name>
    Then Verify response is valid and contains weather information for <response_city_name> with min temp <min_temp> and max temp <max_temp>
    And dependencies are shut down successfully
    Examples:
      | city_name | response_city_name | min_temp | max_temp |
      | chennai | Chennai | 22.0 | 30.20 |
      | london | London | 9 | 11.67 |
      | denver | Denver | 3.33 | 6.67 |
      | bangalore | Bengaluru | 31.11 | 32.78 |