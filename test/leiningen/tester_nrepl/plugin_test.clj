(ns leiningen.tester-nrepl.plugin-test
  (:require [clojure.tools.nrepl :as repl]
            [clojure.tools.nrepl.server :as server]))

(with-open [tserver (server/start-server :port 3456)
            conn (repl/connect :port 3456)]
  (-> (repl/client conn 1000)
      (repl/message {:op :eval :code "(+ 1 1)"})
       repl/response-values))


;server/default-middlewares
