(ns isop.argumentparser
    (:use [isop.core])
    (:use [isop.argumentlexer])
    (:require [clojure.string])
    (:require isop.argumentlexer)
    (:import [isop.argumentlexer Token])
)

(defrecord Parsed [recognized unrecognized])

(defrecord RecognizedArgument [
    ;the "argument" of the expression "--argument"
    argument    
    ;the matched value if any, for instance the "value" of the expression "--argument value"
    value
    ;
    argument-with-options
    ])

(defrecord UnRecognizedArgument [
    ;the "argument" of the expression "--argument"
    argument    
    ;the matched value if any, for instance the "value" of the expression "--argument value"
    value
]) 

(defn build-recognizer [parameters]
    ;vs-arg-pattern = new Regex(@"(?<prefix>\&?)(?<alias>.)[^=:]*(?<equals>[=:]?)")

    (defn create-function [a]
        (fn [argument value] 
            (RecognizedArgument. (:value argument) (:value value) a)
        )
    )
        
    (defn get-parameter-recognizer [a]
        (def vs-arg-pattern #"(\&?)([^=:]*)([=:]?)")

        (let [vs-match (re-find vs-arg-pattern a)]
            (if (nil? vs-match)
                (list a (create-function a))
                (let [[whole is-short name] vs-match
                    vs-fun (create-function a)
                    ]
                    ;(println vs-match)
                    (if (clojure.string/blank? is-short)
                        { name vs-fun}
                        { name vs-fun, (first name) vs-fun}
                    )
                )
            )
        )
    )
    
    (let [recognizers (reduce merge {} (map #(get-parameter-recognizer %) parameters))]
    ;(println (keys recognizers))
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
            (let [[c pot-a] items]
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