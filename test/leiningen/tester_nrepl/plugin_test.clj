(ns leiningen.tester-nrepl.plugin-test
  (:require [clojure.tools.nrepl :as repl]
            [clojure.tools.nrepl.server :as server]
            [leiningen.tester-nrepl :refer [wrap-tester]]))

(midje.config/change-defaults :print-level :print-facts)
(midje.config/change-defaults :emitter 'leiningen.tester-nrepl)

(midje.config/running-in-repl?)

midje.config/*config*

(with-open [tserver (server/start-server :port 3456 :handler (server/default-handler #'wrap-tester))
            conn (repl/connect :port 3456)]
  (-> (repl/client conn 1000)
      ;(repl/message {:op :eval :code "(+ 1 1)"})
      (repl/message {:op "test-midje" :ns-test 'foo.core-test })
      repl/response-values))


;(into [#'wrap-tester] server/default-middlewares)
;(server/default-handler #'wrap-tester)
