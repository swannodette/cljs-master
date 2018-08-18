(ns cljs-master.analyzing
  (:require [clojure.java.io :refer [file reader]]
            [clojure.pprint :refer [pprint]]
            [cljs.analyzer :as ana]
            [cljs.env :as env]))

(def aenv (ana/empty-env))

(def a-form '[1])

(comment

  (pprint
    (env/with-compiler-env (env/default-compiler-env)
      (ana/analyze-form aenv a-form nil nil)))

  )

(def cenv (env/default-compiler-env))
(def a-def '(def foo 1))

(comment

  (pprint
    (env/with-compiler-env cenv
      (ana/analyze-form aenv a-def nil nil)))

  (keys (get-in @cenv [::ana/namespaces]))

  )

;; -----------------------------------------------------------------------------
;; Exercise 1:
;;
;; Lookup foo in ::ana/namespaces.

;; -----------------------------------------------------------------------------
;; Exercise 2:

(def a-defn '(defn foo [a b] (+ a b)))

;; Lookup a a-defn in ::ana/namespaces. How is it different from x?

;; -----------------------------------------------------------------------------
;; Exercise 3:

(def a-defn-2 '(defn foo [a b] (+ a x)))

;; Analyze a-defn-2. What happens? Why? Where in the analyzer does resolution
;; happen?

(comment

  (do
    (env/with-compiler-env cenv
      (ana/analyze-form aenv a-defn-2 nil nil)
      nil))

  )

;; -----------------------------------------------------------------------------
;; Exercise 4:

;; Make a new helper function that uses your cljs-master.analyzing/read-file fn
;; and calls analyze-form.

;; -----------------------------------------------------------------------------
;; Exercise 5 (Extra Credit):

;; Here is an incomplete version of analyze-seq. How is the behavior different
;; form Exercise 2? What's missing. Can you modify it to make it behave the
;; same?

(defn analyze-seq
  ([env form name]
   (analyze-seq env form name
     (when env/*compiler*
       (:options @env/*compiler*))))
  ([env form name opts]
   (if ^boolean (:quoted? env)
     (ana/analyze-list env form)
     (let [line (-> form meta :line)
           line (if (nil? line)
                  (:line env)
                  line)
           col  (-> form meta :column)
           col  (if (nil? col)
                  (:column env)
                  col)
           env  (assoc env :line line :column col)]
       (let [op (first form)]
         (when (nil? op)
           (throw (ana/error env "Can't call nil")))
         (ana/analyze-seq*-wrap op env form name opts))))))

(comment

  (pprint
    (binding [ana/*passes* [ana/elide-env]]
      (env/with-compiler-env cenv
        (analyze-seq aenv a-defn nil nil))))

  )

