(ns binance.request-test
  (:require [clojure.test :refer :all]
            [binance.request :refer :all])
  (:use clj-http.fake))

(deftest test-get-requests
  (testing "Returns Ok"
     (with-fake-routes {
        "https://api.binance.com/api/v3/ticker/bookTicker" (fn [request]
          {:status 200
           :content-type "application/json"
           :body "[]"})}
        (let [response (get-request "/api/v3/ticker/bookTicker")]
          (is (= () response)))))

  (testing "Returns 429"
     (with-fake-routes {
        "https://api.binance.com/api/v3/ticker/bookTicker" (fn [request]
          {:status 429
           :content-type "application/json"
           :body "[]"})}
        (let [response (get-request "/api/v3/ticker/bookTicker")]
          (is (= {:error "request rate limit"} response)))))

  (testing "Returns 418"
     (with-fake-routes {
        "https://api.binance.com/api/v3/ticker/bookTicker" (fn [request]
          {:status 418
           :content-type "application/json"
           :body "[]"})}
        (let [response (get-request "/api/v3/ticker/bookTicker")]
          (is (= {:error "IP has been auto-banned"} response))))))

(deftest test-utils
  (testing "Create query string"
    (let [response (qs {:a 1 :b 2})]
      (is (= "a=1&b=2"
              response))))

  (testing "Create a signature"
    (let [response (signature "123" "a=1&b=2")]
      (is (= "6ec2ad10129cec3f51391df598d41209e647adf5a130a6d9cc2daed9d7f51"
              response))))

  (testing "Create a post complete url"
    (let [response (post-url "/api/v3/ticker/bookTicker" {:a 1 :b 2})]
      (is (= "https://api.binance.com/api/v3/ticker/bookTicker?a=1&b=2&signature=6ec2ad10129cec3f51391df598d41209e647adf5a130a6d9cc2daed9d7f51"
              response)))))

