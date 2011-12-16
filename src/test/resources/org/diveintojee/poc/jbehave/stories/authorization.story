Security story

Meta:
@progress done

Narrative:
In order to provide security capabilities to my service
As a client
I want to clearly get notified when a security event happens

Scenario: requesting a protected resource with correct uid, correct password and insufficient rights should return 403
Given I authenticate with bob uid and bob password
And I receive <responseContentType> data
And I accept <responseLanguage> language
When I request a protected resource that require ADMIN rights
Then I should get an unsuccessful response
And the response code should be 403
And the response message should be <message>

Examples:
responseContentType|responseLanguage|message|
|-- |application/xml|en|Access denied|
|-- |application/xml|fr|Accès refusé|
|application/json|en|Access denied|
|application/json|fr|Accès refusé|

Scenario: requesting a protected resource with correct uid, correct password sufficient authority should return 200
Given I authenticate with admin uid and secret password
When I request a protected resource
Then I should get a successful response
And the response code should be 200
