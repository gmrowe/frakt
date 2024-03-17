(ns frakt.sierpinski
  (:require [frakt.core :as core]
            [frakt.canvas :as canvas]
            [frakt.color :as color]))

(defn draw-triangle
  ;;       v1       v1     v3
  [canvas [[x1 y1] [x2 y2] [x3 y3]] color]
  (-> canvas
      (canvas/draw-line x1 y1 x2 y2 color)
      (canvas/draw-line x1 y1 x3 y3 color)
      (canvas/draw-line x2 y2 x3 y3 color)))

(defn midpoint
  [[x1 y1] [x2 y2]]
  [(int (/ (+ x1 x2) 2.0)) (int (/ (+ y1 y2) 2.0))])
;; .............
;; .....v1......
;; .....*.*.....
;; ...m1...m2....
;; ...*.....*...
;; .v2***m***v3..
;; ......3......
(defn divide
  [[v1 v2 v3]]
  (let [m1 (midpoint v1 v2)
        m2 (midpoint v1 v3)
        m3 (midpoint v2 v3)]
    [[m1 v1 m2] [m1 v2 m3] [m3 v3 m2]]))

(def classic-sierpinski
  (let [width 1200
        height 800
        pad 10
        vertices [[(/ width 2) pad]               ; top-center
                  [pad (- height pad)]            ; bottom-right
                  [(- width pad) (- height pad)]] ; bottom-left
        count 7]
    (reduce (fn [c vs] (draw-triangle c vs color/red))
      (canvas/canvas width height)
      (nth (iterate (fn [vs] (mapcat divide vs)) [vertices]) count))))


(defn main
  [& _args]
  (core/write-canvas-to-ppm-file classic-sierpinski "sierpinski.ppm"))
