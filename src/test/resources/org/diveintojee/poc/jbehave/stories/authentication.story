Security story

Meta:
@progress done

Narrative:
In order to provide security capabilities to my service
As a client
I want to clearly get notified when a security event happens

Scenario: requesting protected resource with wrong uid should fail
Given I authenticate with <uid> uid and whatever password
And I accept <responseLanguage> language
When I request a protected resource
Then I should get an unsuccessful response
And the response code should be 401
And the response message should be <message>

Examples:
|uid|responseLanguage|message|
||en|bad credentials provided|
|unknown-user|en|bad credentials provided|
||fr|informations d'identification incorrectes|
|unknown-user|fr|informations d'identification incorrectes|

Scenario: requesting protected resource with wrong password should fail
Given I authenticate with bob uid and <password> password
And I accept <responseLanguage> language
When I request a protected resource
Then I should get an unsuccessful response
And the response code should be 401
And the response message should be <message>

Examples:
|password|responseLanguage|message|
||en|bad credentials provided|
|unknown-password|en|bad credentials provided|
||fr|informations d'identification incorrectes|
|unknown-password|fr|informations d'identification incorrectes|
