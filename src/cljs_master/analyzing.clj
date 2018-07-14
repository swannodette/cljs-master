(ns cljs-master.analyzing
  (:require [clojure.pprint :refer [pprint]]
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

;; Exercise 1:
;;
;; Lookup x in ::ana/namespaces.


;; Exercise 2:

(def a-defn '(defn foo [a b] (+ a b)))

;; Lookup a a-defn in ::ana/namespaces. How is it different from x?

(comment

  (pprint
    (env/with-compiler-env cenv
      (ana/analyze-form aenv a-defn nil nil)))

  )