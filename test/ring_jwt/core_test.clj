(ns ring-jwt.core-test
  (:require [clojure.test :refer :all]
            [ring-jwt.core :refer :all]
            [cheshire.core :as json]))

(def jwt-header-output "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9")

(def jwt-payload {:iss "joe"
                  :exp 1300819380
                  :blah true})

(def jwt-payload-output "eyJpc3MiOiJqb2UiLCJleHAiOjEzMDA4MTkzODAsImJsYWgiOnRydWV9")

(def jwt-token "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLCJleHAiOjEzMDA4MTkzODAsImJsYWgiOnRydWV9.3GrwsQTbuRxFHiwDjgL7nsibGr2_aC1-4qS_Z9q5RH0")

(def jwt-key "secret")

(deftest test-jwt
  (testing "header"
    (is (= (encode JWT-HEADER) jwt-header-output))
    (is (= (json/parse-string (decode jwt-header-output) true) JWT-HEADER)))
  (testing "payload"
    (is (= (encode jwt-payload) jwt-payload-output))
    (is (= (json/parse-string (decode jwt-payload-output) true) jwt-payload)))
  (testing "token"
    (is (= (make-jwt-token jwt-key jwt-payload) jwt-token))
    (is (= (decode-jwt-payload jwt-key jwt-token) jwt-payload))))
