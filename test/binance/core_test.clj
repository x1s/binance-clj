(ns binance.core-test
  (:require [clojure.test :refer :all]
            [binance.core :refer :all]
            [cheshire.core :as json])
  (:use clj-http.fake))

(deftest test-book-ticker
  (testing "Returns a empty array"
     (with-fake-routes {
        "https://api.binance.com/api/v3/ticker/bookTicker" (fn [request]
          {:status 200
           :content-type "application/json"
           :body (json/generate-string [{:a "world"}])})}
        (is (= [{:a "world"}] (book-ticker))))))
