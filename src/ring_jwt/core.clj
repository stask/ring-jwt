(ns ring-jwt.core
  (:require [clojure.data.codec.base64 :as base64]
            [clojure.string            :as str]
            [cheshire.core             :as json])
  (:import [java.io ByteArrayInputStream ByteArrayOutputStream]))

(def ^:const JWT-HEADER {:typ "JWT" :alg "HS256"})

(def ^:const ALGO "HmacSHA256")

(defprotocol ByteArrayInput
  (input-stream [this]))

(extend-type clojure.lang.IPersistentMap
  ByteArrayInput
  (input-stream [this] (input-stream (json/generate-string this))))

(extend-type String
  ByteArrayInput
  (input-stream [this] (ByteArrayInputStream. (.getBytes this "UTF-8"))))

(extend-type (Class/forName "[B")
  ByteArrayInput
  (input-stream [this] (ByteArrayInputStream. this)))

(defn encode [x]
  (with-open [in  (input-stream x)
              out (ByteArrayOutputStream.)]
    (base64/encoding-transfer in out)
    (-> (String. (.toByteArray out) "UTF-8")
        (str/replace #"\s" "")
        (str/replace "=" "")
        (str/replace "+" "-")
        (str/replace "/" "_"))))

(defn decode [x]
  (let [url-decoded-x (-> (case (mod (count x) 4)
                            2 (str x "==")
                            3 (str x "=")
                            x)
                          (str/replace "-" "+")
                          (str/replace "_" "/"))]
    (with-open [in  (input-stream url-decoded-x)
                out (ByteArrayOutputStream.)]
      (base64/decoding-transfer in out)
      (String. (.toByteArray out) "UTF-8"))))

(defn hmac-sign [secret body]
  (let [key (javax.crypto.spec.SecretKeySpec. (.getBytes secret "UTF-8") ALGO)
        hmac (doto (javax.crypto.Mac/getInstance ALGO)
               (.init key))]
    (-> (.doFinal hmac (.getBytes body "UTF-8"))
        (encode))))

(defn hmac-verify [secret body signature]
  (= signature (hmac-sign secret body)))

(defn parse-jws-token [secret token]
  (let [[encoded-header encoded-payload signature] (str/split token #"\." 3)]
    (if (hmac-verify secret (str/join "." [encoded-header encoded-payload]) signature)
      (json/parse-string (decode encoded-payload) true)
      (throw (IllegalStateException. "failed to validate JWT signature")))))

(defn decode-jwt-payload [secret token]
  (parse-jws-token secret token))

;; ================================================================================
;; public API

(defn make-jwt-token [secret payload]
  (let [data (str/join "." [(encode JWT-HEADER) (encode payload)])
        signature (hmac-sign secret data)]
    (str/join "." [data signature])))

(defn wrap-jwt [handler secret]
  (fn [req]
    (if-let [[[_ token]] (re-seq #"^Bearer\s+(.*)$" (get-in req [:headers "authorization"]))]
      (handler (assoc req :user (decode-jwt-payload secret token)))
      (handler req))))
