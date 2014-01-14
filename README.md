# ring-jwt

Very simple (probably too simple) implementation of [JWT](http://tools.ietf.org/html/draft-ietf-oauth-json-web-token-14).
Only HS256 is supported at the moment.

## Usage

```clojure
;; building the token
(make-jwt-token "secret" {:iss "issuer" :user-id "1234"})

;; using the middleware
(-> ... other middlewares
    (wrap-jwt "secret"))

```

## License

Copyright © 2014 stask.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
