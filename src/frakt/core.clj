(ns frakt.core
  (:require [frakt.canvas :as canvas]
            [clojure.java.io :as io]))

(defn write-canvas-to-ppm-file
  [canvas filename]
  (let [ppm-bytes (canvas/canvas-to-p6-ppm canvas)]
    (with-open [out (io/output-stream filename)] (.write out ppm-bytes))))
