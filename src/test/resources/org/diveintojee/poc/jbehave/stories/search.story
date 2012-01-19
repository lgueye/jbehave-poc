Search story

Meta:
@progress wip

Narrative:
In order to provide search capabilities to my service
As a client
I want to find adverts by multiple criteria

Scenario: finding by exact match should succeed
Given adverts:
|name|description|email|phoneNumber|reference|address.streetAddress|address.city|address.postalCode|address.countryCode|
|Baby phone TBE|Baby phone CHICCO, scope 30m, excellent condition, 10€, pick up on site only|foo009@bar.com|060965234102|REF-75009-001|3 rue Lafayette|Paris 9ème|75009|fr|
|Twin stroller|JANE twin stroller, good condition, 120€, pick up on site only|foo009@bar.com|060968634102|978-2253170570|10 rue Le peletier|Paris 9ème|75009|fr|
|Sherlock Holmes|Sherlock Holmes, excellent condition, BLU-RAY, 5€, 1€ shipping|arcanes@chaos.com|061565234102|B0035WVABS|59 rue de la convention|Paris 15ème|75015|fr|
|Le vol des cigognes|Le vol des cigognes, Jean-Christophe GRANGE, good condition, 3€, 1€ shipping|vol@cigognes.com|062365234102|978-2253170570|118 rue Jean Jaures|Puteaux|92800|fr|
|Ninja Gaiden|Ninja Gaiden 2 features a new and improved game engine, developed from the ground up exclusively for Microsoft and Xbox 360, 5€, 2€ shipping|ninja@gaiden.com|070165234102|B0012PX9HS|9 rue Marcel Monge|Suresnes|92150|fr|
When I receive <responseContentType> data
And I find by exact reference "B0035WVABS" 
Then I should get the following adverts: 
|name|description|email|phoneNumber|reference|address.streetAddress|address.city|address.postalCode|address.countryCode|
|Sherlock Holmes|Sherlock Holmes, excellent condition, BLU-RAY, 5€, 1€ shipping|arcanes@chaos.com|061565234102|B0035WVABS|59 rue de la convention|Paris 15ème|75015|fr|
Then tear down

Examples:
|responseContentType|
|application/json|

Scenario: finding by full text should succeed
Given adverts:
|name|description|email|phoneNumber|reference|address.streetAddress|address.city|address.postalCode|address.countryCode|
|Baby phone TBE|Baby phone CHICCO, scope 30m, excellent condition, 10€, pick up on site only|foo009@bar.com|060965234102|REF-75009-001|3 rue Lafayette|Paris 9ème|75009|fr|
|Twin stroller|JANE twin stroller, good condition, 120€, pick up on site only|foo009@bar.com|060968634102|978-2253170570|10 rue Le peletier|Paris 9ème|75009|fr|
|Sherlock Holmes|Sherlock Holmes, excellent condition, BLU-RAY, 5€, 1€ shipping|arcanes@chaos.com|061565234102|B0035WVABS|59 rue de la convention|Paris 15ème|75015|fr|
|Le vol des cigognes|Le vol des cigognes, Jean-Christophe GRANGE, good condition, 3€, 1€ shipping|vol@cigognes.com|062365234102|978-2253170570|118 rue Jean Jaures|Puteaux|92800|fr|
|Ninja Gaiden|Ninja Gaiden 2 features a new and improved game engine, developed from the ground up exclusively for Microsoft and Xbox 360, 5€, 2€ shipping|ninja@gaiden.com|070165234102|B0012PX9HS|9 rue Marcel Monge|Suresnes|92150|fr|
When I receive <responseContentType> data
And I find by full text description "shipping"
Then I should get the following adverts:
|name|description|email|phoneNumber|reference|address.streetAddress|address.city|address.postalCode|address.countryCode|
|Ninja Gaiden|Ninja Gaiden 2 features a new and improved game engine, developed from the ground up exclusively for Microsoft and Xbox 360, 5€, 2€ shipping|ninja@gaiden.com|070165234102|B0012PX9HS|9 rue Marcel Monge|Suresnes|92150|fr|
|Le vol des cigognes|Le vol des cigognes, Jean-Christophe GRANGE, good condition, 3€, 1€ shipping|vol@cigognes.com|062365234102|978-2253170570|118 rue Jean Jaures|Puteaux|92800|fr|
|Sherlock Holmes|Sherlock Holmes, excellent condition, BLU-RAY, 5€, 1€ shipping|arcanes@chaos.com|061565234102|B0035WVABS|59 rue de la convention|Paris 15ème|75015|fr|
Then tear down

Examples:
|responseContentType|
|application/json|

