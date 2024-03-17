(ns frakt.chaos-sierpinski
  (:require [clojure.math :as math]
            [frakt.core :as core]
            [frakt.canvas :as canvas]
            [frakt.color :as color]))

;; (x1 - x3)(y2 - y2) - (x2 - x3)(y1 - y3)
(defn- sign
  [[x1 y1] [x2 y2] [x3 y3]]
  (math/signum (- (* (- x1 x3) (- y2 y3)) (* (- x2 x3) (- y1 y3)))))

;; Shamelessly stolen from stackoverflow:
;; https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
(defn inside-triangle?
  [p v1 v2 v3]
  (let [d1 (sign p v1 v2)
        d2 (sign p v2 v3)
        d3 (sign p v3 v1)]
    (or (zero? d1) (zero? d2) (zero? d3) (= d1 d2 d3))))

(defn distance-sq
  [x1 y1 x2 y2]
  (+ (* (- x1 x2) (- x1 x2)) (* (- y1 y2) (- y1 y2))))

(defn select-color
  [[[x1 y1] [x2 y2] [x3 y3]] x y]
  (let [v1-dist (distance-sq x y x1 y1)
        v2-dist (distance-sq x y x2 y2)
        v3-dist (distance-sq x y x3 y3)
        min-distance (min v1-dist v2-dist v3-dist)]
    (cond (= min-distance v1-dist) color/red
          (= min-distance v2-dist) color/blue
          (= min-distance v3-dist) color/green)))

(defn midpoint
  [[x1 y1] [x2 y2]]
  [(int (/ (+ x1 x2) 2.0)) (int (/ (+ y1 y2) 2.0))])

(def chaos
  (let [width 1200
        height 800
        pad 10
        vertices [[(/ width 2) pad]               ; top-center
                  [pad (- height pad)]            ; bottom-right
                  [(- width pad) (- height pad)]] ; bottom-left
        starting-point (loop [p [0 0]]
                         (if (apply inside-triangle? p vertices)
                           p
                           (recur [(rand-int width) (rand-int height)])))
        count 100000]
    (reduce (fn [c [x y]]
              (canvas/write-pixel c x y (select-color vertices x y)))
      (canvas/canvas width height)
      (take count
            (iterate (fn [p] (midpoint p (rand-nth vertices)))
                     starting-point)))))
(defn main
  [& _args]
  (core/write-canvas-to-ppm-file chaos "chaos-sierpinski.ppm"))
