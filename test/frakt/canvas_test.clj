(ns frakt.canvas-test
  (:require [clojure.test :refer [is deftest testing]]
            [clojure.string :as str]
            [frakt.canvas :as canvas]
            [frakt.color :as color]))

(def bg-color color/black)
(def fg-color color/red)

(defn canvas->strs
  [c]
  (->> (:canvas/pixels c)
       (map (fn [color]
              (cond (= color fg-color) \*
                    (= color bg-color) \.
                    :else \?)))
       (partition (:canvas/width c))
       (map str/join)))


;; Modify zprint formatting of vectors in the next form for clarity
;!zprint {:format :next :vector {:respect-nl? true}}
(deftest draw-line-test
  (testing "Draw a l-to-r horizontal line"
    (is
      (= ["....."
          ".***."
          "....."]
         (canvas->strs
           (canvas/draw-line (canvas/canvas 5 3 bg-color) 1 1 3 1 fg-color)))))
  (testing "Draw as r-to-l horizontal line"
    (is
      (= ["....."
          ".***."
          "....."]
         (canvas->strs
           (canvas/draw-line (canvas/canvas 5 3 bg-color) 3 1 1 1 fg-color)))))
  (testing "Draw a l-to-r positive slope line where dx = dy"
    (is
      (= ["............."
          ".*..........."
          "..*.........."
          "...*........."
          "....*........"
          ".....*......."
          "............."]
         (canvas->strs
           (canvas/draw-line (canvas/canvas 13 7 bg-color) 1 1 5 5 fg-color)))))
  (testing "Draw a r-to-l positive slope line where dx = dy"
    (is
      (= ["............."
          ".*..........."
          "..*.........."
          "...*........."
          "....*........"
          ".....*......."
          "............."]
         (canvas->strs
           (canvas/draw-line (canvas/canvas 13 7 bg-color) 5 5 1 1 fg-color)))))
  (testing "Draw a l-to-r positive slope line where dx > dy"
    (is (= ["............."
            ".**.........."
            "...**........"
            ".....***....."
            "........**..."
            "..........**."
            "............."]
           (canvas->strs (canvas/draw-line (canvas/canvas 13 7 bg-color)
                                           1
                                           1 11
                                           5 fg-color)))))
  (testing "Draw a r-to-l positive slope line where dx > dy"
    (is (= ["............."
            ".**.........."
            "...**........"
            ".....***....."
            "........**..."
            "..........**."
            "............."]
           (canvas->strs (canvas/draw-line (canvas/canvas 13 7 bg-color)
                                           11
                                           5 1
                                           1 fg-color)))))
  (testing "Draw a l-to-r negative slope line where |dx| > |dy|"
    (is (= ["............."
            "..........**."
            "........**..."
            ".....***....."
            "...**........"
            ".**.........."
            "............."]
           (canvas->strs (canvas/draw-line (canvas/canvas 13 7 bg-color)
                                           1
                                           5 11
                                           1 fg-color)))))
  (testing "Draw a r-to-l negative slope line where |dx| > |dy|"
    (is (= ["............."
            "..........**."
            "........**..."
            ".....***....."
            "...**........"
            ".**.........."
            "............."]
           (canvas->strs (canvas/draw-line (canvas/canvas 13 7 bg-color)
                                           11
                                           1 1
                                           5 fg-color)))))
  (testing "Draw a l-to-r positive slope line where dx < dy"
    (is
      (=
        ["......."
         ".*....."
         ".*....."
         "..*...."
         "..*...."
         "...*..."
         "...*..."
         "...*..."
         "....*.."
         "....*.."
         ".....*."
         ".....*."
         "......."]
        (canvas->strs
          (canvas/draw-line (canvas/canvas 7 13 bg-color) 1 1 5 11 fg-color)))))
  (testing "Draw a r-to-l positive slope line where dx < dy"
    (is
      (=
        ["......."
         ".*....."
         ".*....."
         "..*...."
         "..*...."
         "...*..."
         "...*..."
         "...*..."
         "....*.."
         "....*.."
         ".....*."
         ".....*."
         "......."]
        (canvas->strs
          (canvas/draw-line (canvas/canvas 7 13 bg-color) 1 1 5 11 fg-color)))))
  (testing "Draw a l-to-r negative slope line where |dx| < |dy|"
    (is
      (=
        ["......."
         ".....*."
         ".....*."
         "....*.."
         "....*.."
         "...*..."
         "...*..."
         "...*..."
         "..*...."
         "..*...."
         ".*....."
         ".*....."
         "......."]
        (canvas->strs
          (canvas/draw-line (canvas/canvas 7 13 bg-color) 1 11 5 1 fg-color)))))
  (testing "Draw a r-to-l negative slope line where |dx| < |dy|"
    (is
      (=
        ["......."
         ".....*."
         ".....*."
         "....*.."
         "....*.."
         "...*..."
         "...*..."
         "...*..."
         "..*...."
         "..*...."
         ".*....."
         ".*....."
         "......."]
        (canvas->strs
          (canvas/draw-line (canvas/canvas 7 13 bg-color) 1 11 5 1 fg-color)))))
  (testing "Draw a top to bottom vertial line"
    (is
      (= ["............."
          "......*......"
          "......*......"
          "......*......"
          "......*......"
          "......*......"
          "............."]
         (canvas->strs
           (canvas/draw-line (canvas/canvas 13 7 bg-color) 6 1 6 5 fg-color)))))
  (testing "Draw a bottom to top vertial line"
    (is
      (= ["............."
          "......*......"
          "......*......"
          "......*......"
          "......*......"
          "......*......"
          "............."]
         (canvas->strs
           (canvas/draw-line (canvas/canvas 13 7 bg-color) 6 5 6 1 fg-color)))))
  (testing "Draw a horizoantal line at the bottom of the canvas"
    (is (= ["............."
            "............."
            "............."
            "............."
            "............."
            ".***********."
            "............."])
        (canvas->strs
          (canvas/draw-line (canvas/canvas 13 7 bg-color) 1 5 11 5 fg-color))))
  (testing "Draw multiple lines to form a triangle"
    (is (= ["............."
            "......*......"
            ".....*.*....."
            "....*...*...."
            "...*.....*..."
            "..*********.."
            "............."]
           (canvas->strs (-> (canvas/canvas 13 7 bg-color)
                             (canvas/draw-line 6 1 2 5 fg-color)
                             (canvas/draw-line 2 5 10 5 fg-color)
                             (canvas/draw-line 6 1 10 5 fg-color)))))))
