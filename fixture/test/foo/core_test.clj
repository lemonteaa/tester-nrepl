(ns foo.core-test
  (:require [foo.core :refer :all])
  (:use midje.sweet))

(facts "about factorial"
       (factorial 0) => 1
       (factorial 1) => 1
       (factorial 2) => 3 ;Fake a failure
       (factorial 3) => 6
       (factorial 6) => 720)

(facts "about norm"
       (norm [0 0]) => 0
       (norm [0 7 0]) => 7
       (norm [3 4]) => 5
       (norm [3 -4]) => 5
       (norm [3 4 0 0 0]) => 5)

(facts "about normalize"
       (normalize [5 12]) => [5/13 12/13]
       (normalize [0 0 9 0]) => [0 0 1 0]
       (normalize [3 -4]) => [3/5 -4/5]
       (normalize [0 0 0]) => (throws Exception "norm is zero!"))
