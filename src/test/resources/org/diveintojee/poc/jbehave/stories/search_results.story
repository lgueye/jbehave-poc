Search story

Meta:
@progress
@feature search

Narrative:
In order to provide pagination capabilities to my service
As a client
I want to have control on how results are returned

Scenario: specifying results characteristics should succeed
Given the existing adverts:
|name|description|email|phoneNumber|reference|address.streetAddress|address.city|address.postalCode|address.countryCode|
|Baby phone TBE|Baby phone CHICCO, scope 30m, excellent condition, 10€, pick up on site only|foo009@bar.com|060965234102|REF-75009-001|3 rue Lafayette|Paris 9ème|75009|fr|
|Twin stroller|JANE twin stroller, good condition, 120€, pick up on site only|foo009@bar.com|060968634102|978-2253170570|10 rue Le peletier|Paris 9ème|75009|fr|
|Sherlock Holmes|Sherlock Holmes, excellent condition, BLU-RAY, 5€, 1€ shipping|arcanes@chaos.com|061565234102|B0035WVABS|59 rue de la convention|Paris 15ème|75015|fr|
|Le vol des cigognes|Le vol des cigognes, Jean-Christophe GRANGE, good condition, 3€, 1€ shipping|vol@cigognes.com|062365234102|978-2253170570|118 rue Jean Jaures|Puteaux|92800|fr|
|Ninja Gaiden|Ninja Gaiden 2 features a new and improved game engine, developed from the ground up exclusively for Microsoft and Xbox 360, 5€, 2€ shipping|ninja@gaiden.com|070165234102|B0012PX9HS|9 rue Marcel Monge|Suresnes|92150|fr|
|Drive|Ryan Gosling stars as a Hollywood stunt driver for movies by day and moonlights as a wheelman for criminals by night, 19€, 1€ shipping|drive@gosling.com|070165234102|B0064NTZJO|5, rue kageneck|Strasbourg|67000|fr|
|The Three Musketeers|Logan Lerman and Orlando Bloom star in a swashbuckling update to Alexandre Dumas classic novel, The Three Musketeers, 19€, 1€ shipping|threemusketeers@bloom.com|070165234102|B006P0FIB0|5, rue Jean Lamour|Vandoeuvre|54500|fr|
And I want adverts with "description" containing terms "shipping"
And I configure "9" max results
And I want page "3" 
And I configure "2" items per page
And I receive <responseContentType> data
And I send that search request
Then I should get "9" total items
And I should get "2" items in current page
And the "first" page should be "1"
And the "previous" page should be "2"
And the "next" page should be "4"
And the "last" page should be "5"
Examples:
|responseContentType|
|application/json|

Scenario: not specifying results characteristics should use default ones
Given the existing adverts:
|name|description|email|phoneNumber|reference|address.streetAddress|address.city|address.postalCode|address.countryCode|
|Baby phone TBE|Baby phone CHICCO, scope 30m, excellent condition, 10€, pick up on site only|foo009@bar.com|060965234102|REF-75009-001|3 rue Lafayette|Paris 9ème|75009|fr|
|Twin stroller|JANE twin stroller, good condition, 120€, pick up on site only|foo009@bar.com|060968634102|978-2253170570|10 rue Le peletier|Paris 9ème|75009|fr|
|Sherlock Holmes|Sherlock Holmes, excellent condition, BLU-RAY, 5€, 1€ shipping|arcanes@chaos.com|061565234102|B0035WVABS|59 rue de la convention|Paris 15ème|75015|fr|
|Le vol des cigognes|Le vol des cigognes, Jean-Christophe GRANGE, good condition, 3€, 1€ shipping|vol@cigognes.com|062365234102|978-2253170570|118 rue Jean Jaures|Puteaux|92800|fr|
|Ninja Gaiden|Ninja Gaiden 2 features a new and improved game engine, developed from the ground up exclusively for Microsoft and Xbox 360, 5€, 2€ shipping|ninja@gaiden.com|070165234102|B0012PX9HS|9 rue Marcel Monge|Suresnes|92150|fr|
|Drive|Ryan Gosling stars as a Hollywood stunt driver for movies by day and moonlights as a wheelman for criminals by night, 19€, 1€ shipping|drive@gosling.com|070165234102|B0064NTZJO|5, rue kageneck|Strasbourg|67000|fr|
|The Three Musketeers|Logan Lerman and Orlando Bloom star in a swashbuckling update to Alexandre Dumas classic novel, The Three Musketeers, 19€, 1€ shipping|threemusketeers@bloom.com|070165234102|B006P0FIB0|5, rue Jean Lamour|Vandoeuvre|54500|fr|
And I want adverts with "description" containing terms "shipping"
And I receive <responseContentType> data
And I send that search request
Then I should get "12" total items
And I should get "5" items in current page
And there should be no "first" page
And there should be no "previous" page
And the "next" page should be "2"
And the "last" page should be "3"
Examples:
|responseContentType|
|application/json|
