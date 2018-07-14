(ns cljs-master.compiling
  (:require [cljs.env :as env]
            [cljs.analyzer :as ana]
            [cljs.compiler :as comp]))

(def aenv (ana/empty-env))
(def cenv (env/default-compiler-env))

(def a-defn '(defn foo [a b] (+ a b)))

(comment

  (env/with-compiler-env cenv
    (comp/emit
      (ana/analyze-form aenv a-defn nil nil)))

  )

;; -----------------------------------------------------------------------------
;; Exercise 1:
;;
;; We would like to compile cenv in a different namespace. What do you think
;; we have to change? Examine ana/empty-env to get a clue.

;; -----------------------------------------------------------------------------
;; Exercise 2:

(def a-type '(deftype A []))

(comment

  (env/with-compiler-env cenv
    (comp/emit
      (ana/analyze-form aenv a-type nil nil)))

  )

;; What does deftype compile to? Unfortunately we get a warning? What's your
;; hypothesis about this warning? Have we analyzed core? If not how can
;; we analyze it before we analyze our form?

;; -----------------------------------------------------------------------------
;; Exercise 3:

(def a-defn-2 '(defn bar [c d] (foo c d)))

(comment

  (env/with-compiler-env cenv
    (comp/emit
      (ana/analyze-form aenv a-defn-2 nil nil)))

  )

;; When we compile bar we see the invoke to foo will be cljs.user.foo.call(...)
;; instead of a direct call cljs.user.foo(...). Examine the :invoke case in
;; cljs.compiler, is there something we can do to optimize this call?

;; -----------------------------------------------------------------------------
;; Exercise 4:

(def a-proto '(defprotocol IFoo (-foo [a b])))

(comment

  (env/with-compiler-env cenv
    (comp/emit (ana/analyze-form aenv a-proto nil nil)))

  )

;; This analysis will fail. How we do we fix it? Once fixed describe the
;; the output. What do protocol fns do?

;; -----------------------------------------------------------------------------
;; Exercise 5:

;; Write a function that uses your code from cljs-master.reading and
;; cljs-master.analyzing that will take a file and create an output file of the
;; compiled JavaScript.