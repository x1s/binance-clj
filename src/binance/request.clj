(ns binance.request
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all])
  (:import (javax.crypto Mac)
           (javax.crypto.spec SecretKeySpec)))

(def host
  "https://api.binance.com")

(def secret
  "123")

(defn- parse-payload [payload]
  (parse-string payload true))

(defn- handle-response [payload]
  (case (:status payload)
    200 (parse-payload (:body payload))
    429 {:error "request rate limit"}
    418 {:error "IP has been auto-banned"}
    {:error (str "invalid response status: " (:status payload))
     :message (:body payload) }))

(defn qs [query-map]
  (clojure.string/join "&"
                       (for [[k v] query-map]
                         (str (name k) "=" (java.net.URLEncoder/encode (str v))))))

(defn toHexString [bytes]
  (apply str (map #(format "%x" %) bytes)))

(defn signature [^String secret ^String string]
  (let [mac (Mac/getInstance "HmacSHA256")
        secretSpec (SecretKeySpec. (.getBytes secret) (.getAlgorithm mac))]
    (toHexString (-> (doto mac
                       (.init secretSpec)
                       (.update (.getBytes string)))
                     .doFinal))))

(defn post-url
  [url params]
  (let [query-string (qs params)]
    (str host url
    "?" query-string
    "&signature=" (signature secret query-string))))


(defn get-request
  [url]
  (handle-response
    (client/get
      (str host url)
      {:accept :json :throw-exceptions false})))

(defn post-request
  [url, params]
  (handle-response
    (client/post (post-url url, params))))
