(ns frakt.core
  (:require [frakt.color :as color]
            [frakt.canvas :as canvas]
            [clojure.java.io :as io]
            [clojure.math :as math]))

(defn write-canvas-to-ppm-file
  [canvas filename]
  (let [ppm-bytes (canvas/canvas-to-p6-ppm canvas)]
    (with-open [out (io/output-stream filename)] (.write out ppm-bytes))))


;; y = mx + b
(defn- draw-line-low
  [canvas x0 y0 x1 y1 color]
  (let [start (min x0 x1)
        end (inc (max x0 x1))
        m (/ (- y0 y1) (- x0 x1))
        ;; y = mx + b => b = y - mx
        b (- y0 (* m x0))]
    (reduce (fn [c x] (canvas/write-pixel c x (math/round (+ b (* m x))) color))
      canvas
      (range start end))))

;; x = (y - b) / m
(defn- draw-line-high
  [canvas x0 y0 x1 y1 color]
  (let [start (min y0 y1)
        end (inc (max y0 y1))
        m (/ (- y0 y1) (- x0 x1))
        ;; y = mx + b => b = y - mx
        b (- y0 (* m x0))]
    (reduce (fn [c y] (canvas/write-pixel c (math/round (/ (- y b) m)) y color))
      canvas
      (range start end))))

(defn- draw-line-vert
  [canvas x y0 y1 color]
  (let [start (min y0 y1)
        end (inc (max y0 y1))]
    (reduce (fn [c y] (canvas/write-pixel c x y color))
      canvas
      (range start end))))

;; m = (y0 - y1) / (x0 - x1)
(defn draw-line
  [canvas x0 y0 x1 y1 color]
  (let [dx (- x0 x1)
        dy (- y0 y1)]
    (cond (zero? dx) (draw-line-vert canvas x0 y0 y1 color)
          (>= (abs dx) (abs dy)) (draw-line-low canvas x0 y0 x1 y1 color)
          :else (draw-line-high canvas x0 y0 x1 y1 color))))

;; Shamelessly stolen from stackoverflow:
;; https://stackoverflow.com/questions/2049582/how-to-determine-if-a-point-is-in-a-2d-triangle
;; (x1 - x3)(y2 - y2) - (x2 - x3)(y1 - y3)
(defn- sign
  [[x1 y1] [x2 y2] [x3 y3]]
  (math/signum (- (* (- x1 x3) (- y2 y3)) (* (- x2 x3) (- y1 y3)))))

(defn inside-triangle?
  [p v1 v2 v3]
  (let [d1 (sign p v1 v2)
        d2 (sign p v2 v3)
        d3 (sign p v3 v1)]
    (or (zero? d1) (zero? d2) (zero? d3) (= d1 d2 d3))))

(defn midpoint [[x1 y1] [x2 y2]] [(int (/ (+ x1 x2) 2)) (int (/ (+ y1 y2) 2))])

(defn step
  [canvas [x0 y0] vertices color]
  (let [[x y] (midpoint [x0 y0] (rand-nth vertices))]
    [(canvas/write-pixel canvas x y color) [x y]]))


(def image
  (let [width 1200
        height 800
        pad 25
        v1 [(/ width 2) pad]              ; v1 is at the top center
        v2 [pad (- height pad)]           ; v2 is at the bottom-right
        v3 [(- width pad) (- height pad)] ; v3 is at the bottom left
        canvas (canvas/canvas width height)
        point (loop [p [width height]]
                (if (inside-triangle? p v1 v2 v3)
                  p
                  (recur [(rand-int width) (rand-int height)])))
        count 250000
        fg color/red]
    (first (nth (iterate (fn [[c p]] (step c p [v1 v2 v3] fg)) [canvas point])
                count))))

(defn main [& _args] (write-canvas-to-ppm-file image "frakt-core.ppm"))
