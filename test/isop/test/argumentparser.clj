(ns isop.test.argumentparser
  (:use [isop.argumentparser])
  (:use [clojure.test])
  (:require isop.argumentparser)
  (:import [isop.argumentparser Parsed RecognizedArgument UnRecognizedArgument])
)


;(deftest recognizes_shortform
;    (is (= ((build-recognizer ["&argument"]) (list "-a"))
;        ();    (Parsed. (RecognizedArgument. "a" "") ())
;        )
;    )
;)

(deftest recognizes_longform
    (is (= ((build-recognizer ["beta"]) (list "-a" "--beta"))
             (list (UnRecognizedArgument. "a" nil)
                   (RecognizedArgument. "beta" nil) )
        )
    )
)


