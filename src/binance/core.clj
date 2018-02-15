(ns binance.core
  (:require [binance.api.account :as account]
            [binance.api.order :as order]
            [binance.api.ticker :as ticker]))

(defn book-ticker
  "Get the last ticker information"
  []
  (ticker/book-ticker))

; (defn order
;   "Place a new order"
;   [params]
;   (order/order params))

; (defn account
;   "Retrieve accounts information"
;   [params]
;   (account/account params))
