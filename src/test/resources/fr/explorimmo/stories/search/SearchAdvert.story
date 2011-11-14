Search advert stories

Meta:
@refs 1

Narrative:
Given I provide a search advert request
When I send that request
It should succeed

Scenario: search advert by name should succeed
Given adverts:
|name|description|address.streetAddress|address.city|address.postalCode|
|name0|description0|streetAddress0|paris 18|75018|
|name1|description1|streetAddress1|paris 15|75015|
|name2|description2|streetAddress2|paris 12|75012|
|name3|description3|streetAddress3|paris 11|75011|
|name4|description4|streetAddress4|puteaux|92800|
|name5|description5|streetAddress5|suresnes|92150|
|name6|description6|streetAddress6|issy-les-moulineaux|92130|
|name7|description7|streetAddress7|creteil|94000|
|name8|description8|streetAddress8|joinville-le-pont|94340|
|name9|description9|streetAddress9|saint maur les fosses|94100|
When I search adverts by criteria:
|name|description|address.streetAddress|address.city|address.postalCode|
|name|description|street|city|postal|
Then I get the adverts:
|name|description|address.streetAddress|address.city|address.postalCode|
|name0|description0|streetAddress0|city0|postalCode0|
|name1|description1|streetAddress1|city1|postalCode1|
|name2|description2|streetAddress2|city2|postalCode2|

Scenario: search advert by description should succeed
Scenario: search advert by street adress should succeed
Scenario: search advert by city should succeed
Scenario: search advert by postal code should succeed
Scenario: search advert by country code should succeed
Scenario: search advert without size should fallback to defaultSize
Scenario: search advert without from should fallback to defaultFrom
Scenario: search advert without sort should fallback to defaultSort
