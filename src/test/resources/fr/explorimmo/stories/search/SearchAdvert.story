Search advert stories

Meta:
@search

Narrative:
In order to provide search capabilities to my software
As a visitor 
I want to find adverts by criteria

Scenario: search advert by name should succeed
Given before:
|name|description|email|phoneNumber|address.streetAddress|address.city|address.postalCode|
|Appartement, 3 pièces, 65 m², 450 000€|description0|contact@hoquet.fr|0142010101|streetAddress0|paris 18|75018|
|Appartement, 4 pièces, 82 m², 500 000€|description1|contact@century21.fr|0142010101|streetAddress1|paris 15|75015|
|Atelier, 4 pièces, 82 m², 500 000€|description2|contact@cayla-immo.fr|0142010101|streetAddress2|paris 12|75012|
|Chambre, 4 pièces, 82 m², 500 000€|description3|contact@foncia.fr|0142010101|streetAddress3|paris 11|75011|
|Chalet, 4 pièces, 82 m², 500 000€|description4|contact@orpi.fr|0142010101|streetAddress4|puteaux|92800|
|Duplex, 4 pièces, 82 m², 500 000€|description5|contact@fnaim.fr|0142010101|streetAddress5|suresnes|92150|
|Appartement, 4 pièces, 82 m², 500 000€|description6|contact@villagebleu.fr|0142010101|streetAddress6|issy-les-moulineaux|92130|
|Appartement, 4 pièces, 82 m², 500 000€|description7|contact@laforet.fr|0142010101|streetAddress7|creteil|94000|
|Appartement, 4 pièces, 82 m², 500 000€|description8|contact@hoquet.fr|0142010101|streetAddress8|joinville-le-pont|94340|
|Appartement, 4 pièces, 82 m², 500 000€|description9|contact@hoquet.fr|0142010101|streetAddress9|saint maur les fosses|94100|
When I receive <responseContentType>
And I search adverts by criteria:
|name|
|appart|
Then I should get 6 adverts
Then teardown

Examples:
|responseContentType|
|application/xml|
|application/json|

Scenario: search advert by city should succeed
Given before:
|name|description|email|phoneNumber|address.streetAddress|address.city|address.postalCode|
|Appartement, 3 pièces, 65 m², 450 000€|Appartement, 3 pièces, 65 m², 450 000€|contact@hoquet.fr|0142010101|streetAddress0|paris 18|75018|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@century21.fr|0142010101|streetAddress1|paris 15|75015|
|Atelier, 4 pièces, 82 m², 500 000€|Atelier, 4 pièces, 82 m², 500 000€|contact@cayla-immo.fr|0142010101|streetAddress2|paris 12|75012|
|Chambre, 4 pièces, 82 m², 500 000€|Chambre, 4 pièces, 82 m², 500 000€|contact@foncia.fr|0142010101|streetAddress3|paris 11|75011|
|Chalet, 4 pièces, 82 m², 500 000€|Chalet, 4 pièces, 82 m², 500 000€|contact@orpi.fr|0142010101|streetAddress4|puteaux|92800|
|Duplex, 4 pièces, 82 m², 500 000€|Duplex, 4 pièces, 82 m², 500 000€|contact@fnaim.fr|0142010101|streetAddress5|suresnes|92150|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@villagebleu.fr|0142010101|streetAddress6|issy-les-moulineaux|92130|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@laforet.fr|0142010101|streetAddress7|creteil|94000|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@hoquet.fr|0142010101|streetAddress8|joinville-le-pont|94340|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@hoquet.fr|0142010101|streetAddress9|saint maur les fosses|94100|
When I receive <responseContentType>
And I search adverts by criteria:
|address.city|
|paris|
Then I should get 4 adverts
Then teardown

Examples:
|responseContentType|
|application/xml|
|application/json|

Scenario: search advert by description should succeed
Given before:
|name|description|email|phoneNumber|address.streetAddress|address.city|address.postalCode|
|Appartement, 3 pièces, 65 m², 450 000€|Appartement, 3 pièces, 65 m², 450 000€|contact@hoquet.fr|0142010101|streetAddress0|paris 18|75018|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@century21.fr|0142010101|streetAddress1|paris 15|75015|
|Atelier, 4 pièces, 82 m², 500 000€|Atelier, 4 pièces, 82 m², 500 000€|contact@cayla-immo.fr|0142010101|streetAddress2|paris 12|75012|
|Chambre, 4 pièces, 82 m², 500 000€|Chambre, 4 pièces, 82 m², 500 000€|contact@foncia.fr|0142010101|streetAddress3|paris 11|75011|
|Duplex, 4 pièces, 82 m², 500 000€|Duplex, 4 pièces, 82 m², 500 000€|contact@orpi.fr|0142010101|streetAddress4|puteaux|92800|
|Duplex, 4 pièces, 82 m², 500 000€|Duplex, 4 pièces, 82 m², 500 000€|contact@fnaim.fr|0142010101|streetAddress5|suresnes|92150|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@villagebleu.fr|0142010101|streetAddress6|issy-les-moulineaux|92130|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@laforet.fr|0142010101|streetAddress7|creteil|94000|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@hoquet.fr|0142010101|streetAddress8|joinville-le-pont|94340|
|Appartement, 4 pièces, 82 m², 500 000€|Appartement, 4 pièces, 82 m², 500 000€|contact@hoquet.fr|0142010101|streetAddress9|saint maur les fosses|94100|
When I receive <responseContentType>
And I search adverts by criteria:
|description|
|duplex|
Then I should get 2 adverts
Then teardown

Examples:
|responseContentType|
|application/xml|
|application/json|
