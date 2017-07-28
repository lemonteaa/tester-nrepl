(ns leiningen.tester-nrepl
  (:require [leiningen.util :as u]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

(defn fail
  [transport msg m]
  (case (:type m)
    :actual-result-did-not-match-expected-value
      (u/answer transport msg { :type :test-result
                                :fact 123
                                :result-detail { :result :fail
                                                 :reason :actual-result-did-not-match-expected-value
                                                 :expected-result (:expected-result m)
                                                 :actual (:actual m) }
                                })
    (throw (ex-info "Unknown failure type" { :input-arg m }))))
