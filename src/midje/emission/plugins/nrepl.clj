(ns midje.emission.plugins.nrepl
  (:require [leiningen.util :as u]
            [midje.emission.plugins.default :as default]
            [midje.emission.state :as state]
            [midje.data.fact :as fact]
            [midje.data.nested-facts :as nested-facts]))

(def ^:dynamic *transport* nil)
(def ^:dynamic *msg* nil)

(defn get-fact-hierarchy []
  (map fact/guid nested-facts/*fact-context*))

(defn pass
  []
  (println "Enter: pass")
  ;(println cur-transport)
  (u/answer *transport* *msg*
            { :type :test-result
              :fact-context (get-fact-hierarchy)
              :result-detail { :result :pass }}))

(defn fail
  [m]
  (println "Enter: fail")
  (println m)
  (case (:type m)
    :actual-result-did-not-match-expected-value
      (u/answer *transport* *msg*
                { :type :test-result
                  :fact 123
                  :result-detail { :result :fail
                                   :reason :actual-result-did-not-match-expected-value
                                   :expected-result (:expected-result m)
                                   :actual (:actual m) }
                })
    (throw (ex-info "Unknown failure type" { :input-arg m }))))

(def emission-map (merge default/emission-map
                         { :pass (u/str->fq-var "pass")
                           :fail (u/str->fq-var "fail") }))

(state/install-emission-map-wildly emission-map)

; why we need install-emission-map-wildly above
state/installation-ok?

;check result of installation
state/emission-functions
