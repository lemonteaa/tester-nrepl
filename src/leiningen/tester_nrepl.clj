(ns leiningen.tester-nrepl
  (:require [midje.repl :as midje]
            [midje.emission.plugins.nrepl :as plugin :refer [*transport* *msg*]]))

(defn tester-nrepl
  "I don't do a lot."
  [project & args]
  (println "Hi!"))

;;--------

midje.config/*config*

(midje.config/with-augmented-config { :emitter 'midje.emission.plugins.nrepl
                                      :print-level :print-facts }
  (defn wrap-tester
    [h]
    (fn [{:keys [op transport ns-test] :as msg}]
      (if (= "test-midje" op)
        (binding [*transport* transport
                  *msg* msg]
          (println *transport* *msg*)
          (println (clojure.tools.nrepl.misc/response-for msg { :foo :bar }))
          (println ns-test)
          (Thread/sleep 1000)
          (midje/load-facts (symbol ns-test))
          (Thread/sleep 1000)
          (println "done."))
        (h msg))))
)

;(midje/forget-facts :all)
;(midje/check-facts :all)
