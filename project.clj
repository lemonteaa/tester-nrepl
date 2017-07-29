(defproject tester-nrepl "0.0.1-SNAPSHOT"
  :description "Provide nrepl API to run tests"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [midje "1.8.3"]
                 [org.clojure/tools.nrepl "0.2.12"]]
  :plugins [[refactor-nrepl "1.1.0"]
            [cider/cider-nrepl "0.9.1"]]

  :source-paths ["src" "fixture/src"]
  :test-paths ["test" "fixture/test"])
  ;:eval-in-leiningen true)
