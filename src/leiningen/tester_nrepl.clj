(ns leiningen.tester-nrepl
  (:require [leiningen.util :as u]
            [midje.repl :as midje]
            [midje.emission.plugins.default :as default]
            [midje.emission.state :as state]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

(def ^:dynamic cur-transport nil)
(def ^:dynamic cur-msg nil)

(defn pass
  [transport msg m]
  (println "Enter: pass")
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
                         :pass (partial pass cur-transport cur-msg)))

(state/install-emission-map emission-map)

(midje.config/with-augmented-config { :emitter 'leiningen.tester-nrepl }
  (let [a 'foo.core-test]
      (midje/load-facts a)))

(defn wrap-tester
  [h]
  (fn [{:keys [op transport ns-test] :as msg}]
    (if (= "test-midje" op)
      (binding [cur-transport transport
                cur-msg msg]
        (println cur-transport cur-msg)
        (println ns-test)
        (midje/load-facts (symbol ns-test)))
      (h msg))))

(let [a 'foo.core-test]
(midje/load-facts a))
(midje/load-facts (symbol "foo.core-test"))
(symbol "foo.core-test")
(midje/forget-facts :all)
(midje/check-facts :all)

(defn tt [{:keys [hi]}]
  hi)

(let [b 'joo]
;(= (tt { :hi 'joo}) `'~b))
  `'~b)

