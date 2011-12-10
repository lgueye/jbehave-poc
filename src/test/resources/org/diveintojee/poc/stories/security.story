Security story

Meta:
@done

Narrative:
In order to provide security capabilities to my service
As a client
I want to clearly get notified when a security event happens

Scenario: requesting protected resource with wrong uid should fail
Given I authenticate with <uid> uid and whatever password
And I send <requestContentType> data
And I accept <language> language
And I accept <responseContentType> data
When I request a protected resource
Then I should get an unsuccessfull response
And the response code should be 401
And the error message should be <message>

Examples:
|uid|requestContentType|language|responseContentType|message|
||application/json|en|application/json|bad credentials provided|
|unknown-user|application/json|en|application/json|bad credentials provided|
||application/xml|en|application/json|bad credentials provided|
|unknown-user|application/xml|en|application/json|bad credentials provided|
||application/json|fr|application/json|informations de connexion erronées|
|unknown-user|application/json|fr|application/json|informations de connexion erronées|
||application/xml|fr|application/json|informations de connexion erronées|
|unknown-user|application/xml|fr|application/json|informations de connexion erronées|
