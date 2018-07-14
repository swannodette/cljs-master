(ns cljs-master.closure
  (:require [cljs.env :as env]
            [cljs.analyzer :as ana]
            [cljs.compiler :as comp]))

(def cenv (env/default-compiler-env))
(def aenv (ana/empty-env))

(def a-proto '(defprotocol IFoo (-foo [a b])))

;; -----------------------------------------------------------------------------
;; Exercise 1:

;; Write a function that uses all of your previous code and takes an additional
;; map argument. In this argument the user may supply :optimizations :advanced.
;; If this is the case, before writing out your JavaScript invoke
;; cljs.closure/optimize with this map. Then write out that result. What does
;; the contents of the file look like?