(ns leiningen.tester-nrepl
  (:require [leiningen.util :as u]
            [midje.repl :as midje]
            [midje.emission.plugins.default :as default]
            [midje.emission.state :as state]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

(defn pass
  [transport msg m]
  (println m)
  (u/answer transport msg { :type :test-result
                            :fact 123
                            :result-detail { :result :pass }}))

(defn fail
  [transport msg m]
  (println m)
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

(def emission-map (assoc default/emission-map
                         :fail fail))

(state/install-emission-map emission-map)

(defn wrap-tester
  [h]
  (fn [{:keys [op transport ns-test] :as msg}]
    (if (= "test-midje" op)
      (do (midje/load-facts ns-test))
      (h msg))))

(midje/load-facts 'foo.core-test)
(midje/forget-facts :all)
(midje/check-facts :all)
