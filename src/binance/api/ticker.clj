(ns binance.api.ticker
  (:require [binance.request :as request]))

(defn book-ticker []
  (request/get-request "/api/v3/ticker/bookTicker"))
