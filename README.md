# tester-nrepl

*(Note: Under initial development)*

A [nrepl](https://github.com/clojure/tools.nrepl) extension to provide Testing-as-a-Service (TaaS). ;)

The long term plan is to support all 4 major Clojure testing libraries: clojure.test, [Expectations](https://clojure-expectations.github.io/), [Midje](https://github.com/marick/Midje) and [Speclj](http://speclj.com/).

In the first round we will focus on supporting Midje first, since the [CIDER plugin](https://github.com/clojure-emacs/cider-nrepl) provide the `wrap-test` middleware for testing in clojure.test already.

## Usage

Put `[tester-nrepl "0.0.1-SNAPSHOT"]` into the `:plugins` vector of your `:user`
profile.

## The API (TODO)

*(Tentative plan, subject to change)*

Below is an example reply stream after issuing command to test all facts in Midje:

```
{ :type :start }
{ :type :status-update
  :fact-context [fact1 fact2 ...]
  :status :checking }
    { :type :test-result
      :fact <fact-uuid>
      :result-detail { :result :pass or :fail or :error + other info} }
{ :type :status-update
  :fact-context [fact1 fact2 ...]
  :status :done }
......
{ :type :done }
```

## Contact

This project is currently developed and maintained by @lemonteaa, which can be reached through email listed on the [github account](https://github.com/lemonteaa).

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
