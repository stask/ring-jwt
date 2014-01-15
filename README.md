# ring-jwt

Very simple (probably too simple) implementation of [JWT](http://tools.ietf.org/html/draft-ietf-oauth-json-web-token-14).
Only HS256 is supported at the moment.

## Usage

Use ```(ring-jwt.core/make-jwt-token secret payload)``` to create the JWT somewhere in your login handler. Send the JWT to the client. Client then should pass the token in "Authorization" header like this: ```Authorization: Bearer token```.
_secret_ is a key that you should keep secret. _payload_ is an arbitrary hash that will be encoded into the JWT.

Add the ```ring-jwt.core/wrap-jwt``` middleware to your ring middlewares. It will verify and decode the payload from JWT and assoc it to ring request under _:user_ key. If there is no token, or it couldn't verify the token signature, it will not assoc the payload.

## License

Copyright © 2014 stask.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
