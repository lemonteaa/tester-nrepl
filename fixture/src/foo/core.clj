(ns foo.core
  (:require [clojure.math.numeric-tower :as math]))

(defn factorial
  [n]
  (cond
    (> n 0) (* n (factorial (- n 1)))
    :else 1))

(defn norm
  [x]
  (math/sqrt (reduce + (for [xn x] (math/expt xn 2)))))

;(defn normalize
;  [x]
;  (let [n (norm x)]
;    (if (= n 0)
;      (throw (Exception. "norm is zero!"))
;      (map / x n))))

(defn normalize
  [x]
  (let [n (norm x)]
    (if (= n 0)
      (throw (Exception. "norm is zero!"))
      (for [xn x] (/ xn n)))))
