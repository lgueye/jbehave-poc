Error Handling story

Meta:
@progress done

Narrative:
In order to provide error handling capabilities to my service
As a client
I want to clearly get notified when something goes wrong

Scenario: Same semantic with different contexts should return same http code and different messages
Given I receive <responseContentType> data
When I send a find advert by reference with reference <reference>.
Then I should get an unsuccessful response
And the response code should be 404
And the response message should be <message>

Examples:
|responseContentType|reference|message
|application/json|ref-55555-ermcccvpsole-59999|Advert with reference ref-55555-ermcccvpsole-59999 was not found
|application/json|ref-777|Advert with reference ref-777 was not found

Scenario: Same semantic with same context and different supported languages should return same http code and different messages
Given I receive <responseContentType> data
Given I accept <responseLanguage> language
When I send a find advert by reference with reference <reference>.
Then I should get an unsuccessful response
And the response code should be 404
And the response message should be <message>

Examples:
|responseContentType|responseLanguage|reference|message
|application/json|en|ref-55555-ermcccvpsole-59999|Advert with reference ref-55555-ermcccvpsole-59999 was not found
|application/json|fr|ref-55555-ermcccvpsole-59999|La référence ref-55555-ermcccvpsole-59999 est inconnue

Scenario: Same semantic with same context and different unsupported languages should return same http code and same messages (fallback language)
Given I receive <responseContentType> data
Given I accept <responseLanguage> language
When I send a find advert by reference with reference <reference>.
Then I should get an unsuccessful response
And the response code should be 404
And the response message should be <message>

Examples:
|responseContentType|responseLanguage|reference|message
|application/json|it|ref-55555-ermcccvpsole-59999|Advert with reference ref-55555-ermcccvpsole-59999 was not found
|application/json|es|ref-777|Advert with reference ref-777 was not found
