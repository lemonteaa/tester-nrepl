(ns midje.emission.plugins.nrepl
  (:require [leiningen.util :as u]
            [midje.emission.plugins.default :as default]
            [midje.emission.state :as state]
            [midje.data.fact :as fact]
            [midje.data.nested-facts :as nested-facts]))

(def ^:dynamic *transport* nil)
(def ^:dynamic *msg* nil)

(defn starting-fact-stream []
  (u/answer *transport* *msg*
            { :type :start }))

(defn finishing-fact-stream []
  (u/answer *transport* *msg*
            { :type :done }))

(defn get-fact-hierarchy []
  nested-facts/*fact-context*)

(defn starting-to-check-fact [f]
  (u/answer *transport* *msg*
            { :type :status-update :fact-context (get-fact-hierarchy) :status :checking }))

(defn finishing-fact [f]
  (u/answer *transport* *msg*
            { :type :status-update :fact-context (get-fact-hierarchy) :status :done }))

(defn pass
  []
  (println "Enter: pass")
  ;(println cur-transport)
  (u/answer *transport* *msg*
            { :type :test-result
              :fact-context (get-fact-hierarchy)
              :result-detail { :result :pass }}))

(def fail-reasons
  #{ :actual-result-did-not-match-expected-value
     :actual-result-should-not-have-matched-expected-value
     :actual-result-did-not-match-checker
     :actual-result-should-not-have-matched-checker
     :some-prerequisites-were-called-the-wrong-number-of-times })

(defn fail
  [m]
  (println "Enter: fail")
  (println m)
  (u/answer *transport* *msg*
            { :type :test-result
              :fact 123
              :result-detail (assoc (clojure.set/rename-keys m { :type :reason })
                                     :result (if (contains? fail-reasons (:type m))
                                                :fail
                                                :error))
            }))

(def emission-map (merge default/emission-map
                         { :pass (u/str->fq-var "pass")
                           :fail (u/str->fq-var "fail")
                           :starting-fact-stream (u/str->fq-var "starting-fact-stream")
                           :finishing-fact-stream (u/str->fq-var "finishing-fact-stream")
                           :starting-to-check-fact (u/str->fq-var "starting-to-check-fact")
                           :finishing-fact (u/str->fq-var "finishing-fact") }))

(state/install-emission-map-wildly emission-map)

; why we need install-emission-map-wildly above
state/installation-ok?

;check result of installation
state/emission-functions
