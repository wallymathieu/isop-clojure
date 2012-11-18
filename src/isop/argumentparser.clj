(ns isop.argumentparser
    (:use [isop.core])
    (:use [isop.argumentlexer])
    (:require [clojure.string])
    (:require isop.argumentlexer)
    (:import [isop.argumentlexer Token])
)
;(:require [clojure.set])
;(require clojure.set)
;(:import [clojure.set select])


(defrecord Parsed [recognized unrecognized])

(defrecord RecognizedArgument [
    ;the "argument" of the expression "--argument"
    argument    
    ;the matched value if any, for instance the "value" of the expression "--argument value"
    value
    ;
    ;argument-with-options
    ])

(defrecord UnRecognizedArgument [
    ;the "argument" of the expression "--argument"
    argument    
    ;the matched value if any, for instance the "value" of the expression "--argument value"
    value
]) 

(defn build-recognizer [parameters]
    (defn get-parameter-recognizer [a]
        (fn [argument value] 
            (RecognizedArgument. (:value argument) (:value value))
        )
    )
    
    (let [recognizers (apply #(hash-map % (get-parameter-recognizer %)) parameters)]
    
    (defn recognize [p1 p2]
        (let [p (get recognizers (:value p1))]
            (if (nil? p)
                (UnRecognizedArgument. (:value p1) (:value p2)) 
                (p p1 p2)
            )
        )
    )
    
    (defn map-recognize-tree [items]
        (if (empty? items)
            ()
            (let [c (first items) pot-a (second items)]
                (if (and 
                        (not (nil? pot-a))
                        (= :parameter (:type c))
                        (= :argument (:type pot-a))
                    )
                    (cons (recognize c pot-a)
                        (map-recognize-tree (nthrest items 2))
                    )
                    (cons (recognize c nil) 
                        (map-recognize-tree (rest items))
                    )
                )
            )
        )
    )

    (fn [arguments]
        (let [lexed (argumentlexer-lex arguments)]
            (map-recognize-tree lexed)
        )
    )
    )
)