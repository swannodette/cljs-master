(ns cljs-master.reading
  (:require [clojure.java.io :as io]
            [clojure.tools.reader :as reader]
            [clojure.tools.reader.reader-types :as readers]
            [cljs.analyzer :as ana])
  (:import [java.io Reader StringReader PushbackReader]))

(def a-list "(foo :bar baz) (1 [2] {3 #{}})")

(defn forms-seq
  "Given a reader return a lazy sequence of read forms."
  [^Reader rdr]
  (let [eof-sentinel (Object.)
        opts         {:eof eof-sentinel}
        pbr          (readers/indexing-push-back-reader
                       (PushbackReader. rdr) 1 "<no-filename>")
        forms-seq*   (fn forms-seq* []
                       (lazy-seq
                         (let [form (binding [*ns* (create-ns ana/*cljs-ns*)
                                              reader/resolve-symbol ana/resolve-symbol]
                                      (reader/read opts pbr))]
                           (if (identical? form eof-sentinel)
                             (.close rdr)
                             (cons form (forms-seq*))))))]
    (forms-seq*)))

(comment

  (def rdr (StringReader. a-list))

  (forms-seq rdr)

  )

;; Exercise 1:
;;
;; Write a function that uses form-seq that given a file will return a lazy
;; sequence of forms

(def a-list "(foo ::a/bar baz) (1 [2] {3 #{}})")

;; Exercise 2:
;;
;; Try to read the above form. It will fail. Write a version of form-seq that
;; can expand aliases, feel free to refer to cljs.analyzer/forms-seq* to figure
;; this out. reader/*alias-map* is map of Symbol -> Symbol.

;; Exercise 3 (Extra Credit):
;;
;; Write a version of forms-seq that can handle conditional reading. Make it
;; so that you can choose to read :clj or :cljs.

;; Exercise 4 (Extra Credit):
;;
;; Examine cljs.repl/source-fn. Write a version of of form-seq that does source
;; logging. Check the meta of the read form the confirm that you have access
;; to the source.