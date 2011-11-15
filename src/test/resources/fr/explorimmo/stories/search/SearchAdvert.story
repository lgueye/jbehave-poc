Search advert stories

Meta:
@refs 1
@search

Narrative:
Given I provide a search advert request
When I send that request
It should succeed

Scenario: search advert by name should succeed
Given adverts:
|name|description|email|phoneNumber|address.streetAddress|address.city|address.postalCode|
|name0|description0|contact@hoquet.fr|0142010101|streetAddress0|paris 18|75018|
|name1|description1|contact@century21.fr|0142010101|streetAddress1|paris 15|75015|
|name2|description2|contact@cayla-immo.fr|0142010101|streetAddress2|paris 12|75012|
|name3|description3|contact@foncia.fr|0142010101|streetAddress3|paris 11|75011|
|name4|description4|contact@orpi.fr|0142010101|streetAddress4|puteaux|92800|
|name5|description5|contact@fnaim.fr|0142010101|streetAddress5|suresnes|92150|
|name6|description6|contact@villagebleu.fr|0142010101|streetAddress6|issy-les-moulineaux|92130|
|name7|description7|contact@laforet.fr|0142010101|streetAddress7|creteil|94000|
|name8|description8|contact@hoquet.fr|0142010101|streetAddress8|joinville-le-pont|94340|
|name9|description9|contact@hoquet.fr|0142010101|streetAddress9|saint maur les fosses|94100|
When I receive <responseContentType>
And I search adverts by criteria:
|name|description|address.streetAddress|
|name|description|street|
Then I sould get the adverts:
|name|description|address.streetAddress|address.city|address.postalCode|
|name0|description0|streetAddress0|city0|postalCode0|
|name1|description1|streetAddress1|city1|postalCode1|
|name2|description2|streetAddress2|city2|postalCode2|
Examples:
|responseContentType|
|application/xml|
|application/json|

Scenario: search advert by description should succeed
Scenario: search advert by street adress should succeed
Scenario: search advert by city should succeed
Scenario: search advert by postal code should succeed
Scenario: search advert by country code should succeed
Scenario: search advert without size should fallback to defaultSize
Scenario: search advert without from should fallback to defaultFrom
Scenario: search advert without sort should fallback to defaultSort
