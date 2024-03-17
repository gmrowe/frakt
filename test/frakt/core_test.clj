(ns frakt.core-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.string :as str]
            [frakt.color :as color]
            [frakt.canvas :as canvas]
            [frakt.core :as core]))

;!zprint {:format :next :comment {:smart-wrap? false}}
(deftest inside-triangle?-test
  ;;            111
  ;;  0123456789012
  ;; 0.............
  ;; 1......*......
  ;; 2.....*.*..b..
  ;; 3....*..a*....
  ;; 4...c.....*...
  ;; 5..*********..
  ;; 6.............
  (let [[v1 v2 v3] [[6 1] [2 5] [10 5]]
        a [7 3]
        b [10 2]
        c [3 4]]
    (testing "Point `a` is inside the triangle vertices"
      (is (core/inside-triangle? a v1 v2 v3)))
    (testing "Point `b` is outside the triangle vertices"
      (is (not (core/inside-triangle? b v1 v2 v3))))
    ;; For our purposes we will allow an edge to be in the triangle
    (testing "Point `c` is on an edge of the triangle"
      (is (core/inside-triangle? c v1 v2 v3)))))
